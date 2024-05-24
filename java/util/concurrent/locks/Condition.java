/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent.locks;
import java.util.concurrent.TimeUnit;
import java.util.Date;

/**
 * {@code Condition} factors out the {@code Object} monitor
 * methods ({@link Object#wait() wait}, {@link Object#notify notify}
 * and {@link Object#notifyAll notifyAll}) into distinct objects to
 * give the effect of having multiple wait-sets per object, by
 * combining them with the use of arbitrary {@link Lock} implementations.
 * Where a {@code Lock} replaces the use of {@code synchronized} methods
 * and statements, a {@code Condition} replaces the use of the Object
 * monitor methods.
 *
 * Condition接口将Object监视器方法（例如wait、notify和notifyAll）分离出来成为独立的对象，
 * 以此实现了每个对象可拥有多个等待集合的效果。这是通过将Condition与任意Lock实现结合使用来实现的。
 * 当Lock取代了synchronized方法和语句的使用时，Condition则替代了Object监视器方法的作用。
 *
 * <p>Conditions (also known as <em>condition queues</em> or
 * <em>condition variables</em>) provide a means for one thread to
 * suspend execution (to &quot;wait&quot;) until notified by another
 * thread that some state condition may now be true.  Because access
 * to this shared state information occurs in different threads, it
 * must be protected, so a lock of some form is associated with the
 * condition. The key property that waiting for a condition provides
 * is that it <em>atomically</em> releases the associated lock and
 * suspends the current thread, just like {@code Object.wait}.
 *
 <p>`条件`(也称为<em>条件队列</em>或<em>条件变量</em>)提供了一种机制，
 使得一个线程能够暂停执行（即“等待”），直到另一个线程通知它某些状态条件可能已经变为真。
 由于对这个共享状态信息的访问发生在不同的线程中，因此必须对其进行保护，所以会有一个锁与这个条件相关联。
 等待条件的关键特性是，它能够<em>原子性</em>地释放关联的锁并挂起当前线程，这与`Object.wait`方法的行为一致。
 *
 * <p>A {@code Condition} instance is intrinsically bound to a lock.
 * To obtain a {@code Condition} instance for a particular {@link Lock}
 * instance use its {@link Lock#newCondition newCondition()} method.
 *
 * <p>As an example, suppose we have a bounded buffer which supports
 * {@code put} and {@code take} methods.  If a
 * {@code take} is attempted on an empty buffer, then the thread will block
 * until an item becomes available; if a {@code put} is attempted on a
 * full buffer, then the thread will block until a space becomes available.
 * We would like to keep waiting {@code put} threads and {@code take}
 * threads in separate wait-sets so that we can use the optimization of
 * only notifying a single thread at a time when items or spaces become
 * available in the buffer. This can be achieved using two
 * {@link Condition} instances.
 *
 * <p>`Condition`实例本质上是与一个锁绑定的。为了获取特定`Lock`实例的`Condition`实例，
 * 应使用其`Lock#newCondition`方法。 <p>举个例子，假设我们有一个有界缓冲区，
 * 支持`put`和`take`方法。如果在一个空缓冲区上尝试`take`操作，那么线程将阻塞，
 * 直到有项目可用；如果在一个已满的缓冲区上尝试`put`操作，那么线程将阻塞，直到有空间可用。
 * 我们希望将等待的`put`线程和`take`线程保持在不同的等待集中，这样当缓冲区中有项目或空间可用时，
 * 我们可以优化仅通知单个线程。这可以通过使用两个`Condition`实例来实现。
 * <pre>
 * class BoundedBuffer {
 *   <b>final Lock lock = new ReentrantLock();</b>
 *   final Condition notFull  = <b>lock.newCondition(); </b>
 *   final Condition notEmpty = <b>lock.newCondition(); </b>
 *
 *   final Object[] items = new Object[100];
 *   int putptr, takeptr, count;
 *
 *   public void put(Object x) throws InterruptedException {
 *     <b>lock.lock();
 *     try {</b>
 *       while (count == items.length)
 *         <b>notFull.await();</b>
 *       items[putptr] = x;
 *       if (++putptr == items.length) putptr = 0;
 *       ++count;
 *       <b>notEmpty.signal();</b>
 *     <b>} finally {
 *       lock.unlock();
 *     }</b>
 *   }
 *
 *   public Object take() throws InterruptedException {
 *     <b>lock.lock();
 *     try {</b>
 *       while (count == 0)
 *         <b>notEmpty.await();</b>
 *       Object x = items[takeptr];
 *       if (++takeptr == items.length) takeptr = 0;
 *       --count;
 *       <b>notFull.signal();</b>
 *       return x;
 *     <b>} finally {
 *       lock.unlock();
 *     }</b>
 *   }
 * }
 * </pre>
 *
 * (The {@link java.util.concurrent.ArrayBlockingQueue} class provides
 * this functionality, so there is no reason to implement this
 * sample usage class.)
 *
 * <p>A {@code Condition} implementation can provide behavior and semantics
 * that is
 * different from that of the {@code Object} monitor methods, such as
 * guaranteed ordering for notifications, or not requiring a lock to be held
 * when performing notifications.
 * If an implementation provides such specialized semantics then the
 * implementation must document those semantics.
 (java.util.concurrent.ArrayBlockingQueue类已经提供了这种功能，因此没有必要实现这个示例用途类。)

 * <p>`Condition`实现可以提供与`Object`监视器方法不同的行为和语义，比如保证通知的顺序性，
 * 或者在执行通知时不需要持有锁。 如果实现提供了这样的特殊语义，
 * 那么该实现必须在文档中明确这些行为和要求，以便程序员能够正确和有效地使用它。
 * 这意味着用户在选择和使用`Condition`实现时，应当仔细阅读相关文档，
 * 理解其实现的具体特性和限制。
 * <p>Note that {@code Condition} instances are just normal objects and can
 * themselves be used as the target in a {@code synchronized} statement,
 * and can have their own monitor {@link Object#wait wait} and
 * {@link Object#notify notification} methods invoked.
 * Acquiring the monitor lock of a {@code Condition} instance, or using its
 * monitor methods, has no specified relationship with acquiring the
 * {@link Lock} associated with that {@code Condition} or the use of its
 * {@linkplain #await waiting} and {@linkplain #signal signalling} methods.
 * It is recommended that to avoid confusion you never use {@code Condition}
 * instances in this way, except perhaps within their own implementation.
 *<p>请注意，`Condition`实例只是普通的对象，它们自身也可以作为`synchronized`语句的目标，
 * 并且可以对其自身的监视器`Object#wait`和`Object#notify`通知方法进行调用。
 * 但是，获取`Condition`实例的监视器锁，或使用其监视器方法，
 * 与获取与该`Condition`相关的`Lock`或使用其`await`等待和`signal`信号方法之间没有指定的关系。
 * 为了避免混淆，建议你不要以这种方式使用`Condition`实例，
 * 除非可能在它们自己的实现内部。
 * 这样做可以确保代码的清晰度和避免不必要的并发问题。
 * <p>Except where noted, passing a {@code null} value for any parameter
 * will result in a {@link NullPointerException} being thrown.
 *
 * <h3>Implementation Considerations</h3>
 *
 * <p>When waiting upon a {@code Condition}, a &quot;<em>spurious
 * wakeup</em>&quot; is permitted to occur, in
 * general, as a concession to the underlying platform semantics.
 * This has little practical impact on most application programs as a
 * {@code Condition} should always be waited upon in a loop, testing
 * the state predicate that is being waited for.  An implementation is
 * free to remove the possibility of spurious wakeups but it is
 * recommended that applications programmers always assume that they can
 * occur and so always wait in a loop.
 * <p>除非特别说明，对于任何参数传递`null`值都将导致抛出`NullPointerException`异常。
 * <h3>实现注意事项</h3> <p>当在一个`Condition`上等待时，允许发生所谓的“<em>虚假唤醒</em>”现象，
 * 这通常是向底层平台语义做出的让步。这对于大多数应用程序来说实际影响很小，
 * 因为`Condition`应该总是在循环中等待，并测试正在等待的状态条件。
 * 实现可以自由地消除虚假唤醒的可能性，但强烈建议应用程序开发者始终假设它们可能发生，
 * 并因此始终在循环中等待。
 * 如下示例所示：
 * lock.lock();
 * try {
 *     while (!conditionIsMet()) {
 *         condition.await();
 *     }
 *     // 条件满足时执行的代码
 * } finally {
 *     lock.unlock();
 * }
 * <p>The three forms of condition waiting
 * (interruptible, non-interruptible, and timed) may differ in their ease of
 * implementation on some platforms and in their performance characteristics.
 * In particular, it may be difficult to provide these features and maintain
 * specific semantics such as ordering guarantees.
 * Further, the ability to interrupt the actual suspension of the thread may
 * not always be feasible to implement on all platforms.
 * <p>三种形式的条件等待（可中断、不可中断和定时）在某些平台上的实现难易程度及性能特征可能会有所不同。
 * 特别是，同时提供这些功能并保持特定的语义（如顺序保证）可能较为困难。
 * 此外，并非所有平台上都能实现实际中断线程挂起的能力。
 * 这意味着不同操作系统和JVM实现可能会在如何高效、准确地提供这三种等待方式上有所差异
 * 。开发者需要注意的是，尽管Java并发库设计意图在于提供跨平台一致的行为，但在极端情况下或特定平台上，
 * 这些等待形式的效率和具体表现可能会有所不同。特别是，中断线程等待的能力可能受限于底层操作系统提供的功能。
 * 因此，在设计并发程序时，了解目标部署平台的特性和限制也是很重要的。
 * <p>Consequently, an implementation is not required to define exactly the
 * same guarantees or semantics for all three forms of waiting, nor is it
 * required to support interruption of the actual suspension of the thread.
 *
 * <p>An implementation is required to
 * clearly document the semantics and guarantees provided by each of the
 * waiting methods, and when an implementation does support interruption of
 * thread suspension then it must obey the interruption semantics as defined
 * in this interface.

 *<p>因此，实现不必为所有三种等待形式定义完全相同的保证或语义，也不必支持线程挂起的实际中断。
 *<p>但是，实现被要求清晰地记录每种等待方法所提供的语义和保证。当实现确实支持线程挂起的中断时，
 *  它必须遵守本接口定义的中断语义。
 *简而言之，虽然Java的Condition接口为等待操作提供了标准的API，
 * 但具体实现（特别是在不同的JVM和操作系统上）在如何处理可中断等待、不可中断等待以及定时等待的细节上可能存在差异。
 * 开发者在使用这些方法时，应当参考所使用的JVM文档来了解其实现的具体语义和保证，特别是在设计高度依赖这些特性的并发程序时。
 * 如果实现支持中断，那么它必须确保中断行为遵循Condition接口规定的规则，
 * 以保证跨平台的兼容性和预期的行为一致性。

 * <p>As interruption generally implies cancellation, and checks for
 * interruption are often infrequent, an implementation can favor responding
 * to an interrupt over normal method return. This is true even if it can be
 * shown that the interrupt occurred after another action that may have
 * unblocked the thread. An implementation should document this behavior.
 *<p>由于中断通常意味着取消，并且检查中断的情况往往不频繁，因此实现可以优先响应中断而不是正常的方法返回。
 * 这一点即便在可以证明中断发生在可能已经解除线程阻塞的另一个动作之后的情况下也同样适用。实现应当记录这一行为。
 * 这意味着，在设计Condition的实现时，考虑到中断常常与任务取消相关联，
 * 实现者可以选择在检测到中断信号时立即响应，哪怕这意味着中断实际上是在某些其他操作（这些操作可能导致线程已经准备好继续执行）之后发生的。
 * 这样做是为了快速响应线程取消的需求，避免不必要的延迟。然而，由于这种行为可能会影响程序的逻辑和预期流程，
 * 实现者应当在其文档中明确说明这种中断处理策略，以便程序员能够据此做出正确的编程决策。
 * @since 1.5
 * @author Doug Lea
 */
public interface Condition {

    /**
     * Causes the current thread to wait until it is signalled or
     * {@linkplain Thread#interrupt interrupted}.
     *
     * <p>The lock associated with this {@code Condition} is atomically
     * released and the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until <em>one</em> of four things happens:
     * <ul>
     * <li>Some other thread invokes the {@link #signal} method for this
     * {@code Condition} and the current thread happens to be chosen as the
     * thread to be awakened; or
     * <li>Some other thread invokes the {@link #signalAll} method for this
     * {@code Condition}; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of thread suspension is supported; or
     * <li>A &quot;<em>spurious wakeup</em>&quot; occurs.
     * </ul>
     *导致当前线程等待，直到它被信号唤醒或{@linkplain Thread#interrupt interrupted}中断。
     *
     * 与这个Condition相关的锁会被原子性地释放，当前线程为了线程调度目的而变得不可用，处于休眠状态，直到以下四件事情之一发生：
     *
     * <ul> <li>其他某个线程调用了此`Condition`的`{@link #signal}`方法，并且当前线程被选中为要唤醒的线程；
     * 或者 <li>其他某个线程调用了此`Condition`的`{@link #signalAll}`方法；
     * 或者 <li>其他某个线程`{@linkplain Thread#interrupt}`中断了当前线程，并且支持线程挂起的中断；
     * 或者 <li>发生了“<em>虚假唤醒</em>”现象。 </ul>
     * 这段描述概括了Condition的await()方法的工作原理：它会使调用它的线程进入等待状态，直到接收到特定的信号、
     * 被所有等待的线程一起唤醒、被中断（如果实现支持中断挂起的线程），或者遭遇虚假唤醒。
     * 在任何一种情况下，线程醒来后，通常会重新尝试获取关联的锁，并根据条件判断是否继续执行或再次等待。
     *
     * <p>In all cases, before this method can return the current thread must
     * re-acquire the lock associated with this condition. When the
     * thread returns it is <em>guaranteed</em> to hold this lock.
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while waiting
     * and interruption of thread suspension is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared. It is not specified, in the first
     * case, whether or not the test for interruption occurs before the lock
     * is released.
     *
     * 在所有情况下，此方法能够返回之前，当前线程必须重新获取与此条件关联的锁。当线程返回时，它<em>保证</em>持有这个锁。
     *
     * 如果当前线程：
     *
     * <ul> <li>在进入此方法时其中断状态已经被设置；或者 <li>在等待过程中被中断并且支持中断线程挂起，
     * </ul> 那么会抛出`{@link InterruptedException}`，并且当前线程的中断状态会被清除。
     * 在第一种情况下，是否在释放锁之前检测中断状态并未明确规定。
     * 这意味着，调用await()方法的线程在被唤醒或因中断退出等待状态后，都会自动尝试重新获取锁，
     * 并且在方法返回前一定能获得该锁。同时，如果线程在等待时被中断，它将抛出异常并清除中断标志，
     * 尽管具体是在何时检查中断状态（释放锁之前或之后）未做明确说明，这取决于具体实现
     * 。因此，开发人员在处理中断逻辑时，应做好相应的异常处理准备。
     * <p><b>Implementation Considerations</b>
     *
     * <p>The current thread is assumed to hold the lock associated with this
     * {@code Condition} when this method is called.
     * It is up to the implementation to determine if this is
     * the case and if not, how to respond. Typically, an exception will be
     * thrown (such as {@link IllegalMonitorStateException}) and the
     * implementation must document that fact.
     *
     * <p>An implementation can favor responding to an interrupt over normal
     * method return in response to a signal. In that case the implementation
     * must ensure that the signal is redirected to another waiting thread, if
     * there is one.
     *
     * <p><b>实现注意事项</b> <p>调用此方法时，假定当前线程持有与此`Condition`关联的锁。
     * 是否确实如此，以及如果不是，应该如何响应，由实现决定。
     * 通常，会抛出异常（如`{@link IllegalMonitorStateException}`），并且实现必须记录这一行为。
     * <p>实现可以选择在响应信号时优先处理中断而非正常方法返回。在这种情况下，
     * 实现必须确保信号被重定向给另一个等待的线程（如果存在的话）。
     这意味着，虽然调用await()时理应已经持有了相应的锁，但具体的实现可以自行检查这一点，
     并在必要时采取适当的措施，比如抛出异常。此外，实现具有一定的灵活性，可以设计为在接收到唤醒信号时优先考虑中断的处理，
     但这要求实现确保如果有其他线程也在等待，那么信号不会丢失，应能传递给下一个合适的等待线程。
     因此，开发者在使用Condition时，应当熟悉所使用实现的具体行为和要求，以确保正确地处理并发控制和线程中断逻辑。


     * @throws InterruptedException if the current thread is interrupted
     *         (and interruption of thread suspension is supported)
     */
    void await() throws InterruptedException;

    /**
     * Causes the current thread to wait until it is signalled.
     *
     * <p>The lock associated with this condition is atomically
     * released and the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until <em>one</em> of three things happens:
     * <ul>
     * <li>Some other thread invokes the {@link #signal} method for this
     * {@code Condition} and the current thread happens to be chosen as the
     * thread to be awakened; or
     * <li>Some other thread invokes the {@link #signalAll} method for this
     * {@code Condition}; or
     * <li>A &quot;<em>spurious wakeup</em>&quot; occurs.
     * </ul>
     *导致当前线程等待，直到它被信号唤醒。
     *
     * 与这个条件相关的锁会被原子性地释放，当前线程为了线程调度目的而变得不可用，
     * 处于休眠状态，直到以下三件事情之一发生：
     *
     * <ul> <li>其他某个线程调用了此`Condition`的`{@link #signal}`方法，并且当前线程被选中为要唤醒的线程；
     * 或者 <li>其他某个线程调用了此`Condition`的`{@link #signalAll}`方法；或者 <li>发生了“<em>虚假唤醒</em>”现象。 </ul>
     * 这是对Condition等待机制的一种简化描述，忽略了中断的情况。实际上，在很多实现中，如果线程在等待期间被中断，即使没有显式列出，
     * 也会作为导致等待结束的一个因素。但这里的重点是阐述线程等待直至被明确信号唤醒的基本过程，以及虚假唤醒的可能性。
     *
     * <p>In all cases, before this method can return the current thread must
     * re-acquire the lock associated with this condition. When the
     * thread returns it is <em>guaranteed</em> to hold this lock.
     *
     * <p>If the current thread's interrupted status is set when it enters
     * this method, or it is {@linkplain Thread#interrupt interrupted}
     * while waiting, it will continue to wait until signalled. When it finally
     * returns from this method its interrupted status will still
     * be set.
     *在所有情况下，此方法能够返回之前，当前线程必须重新获取与此条件关联的锁。当线程返回时，
     * 它<em>保证</em>持有这个锁。
     *
     * 如果当前线程在进入此方法时中断状态已被设置，或者在等待过程中被中断，它将继续等待直到被信号唤醒。
     * 当它最终从这个方法返回时，其中断状态仍然会被设置。
     *
     * 这意味着，即使线程在调用awaitNanos(long)方法时已经中断，或者在等待过程中被中断，
     * 它也不会立即抛出InterruptedException，而是会无视中断状态继续等待，直到被正常唤醒或者等待超时。
     * 只有当从方法返回时，中断状态才会保持不变，允许调用者检查并适当地处理中断情况。这是一种不同于await()的行为，
     * 后者在遇到中断时会抛出异常并清除中断状态。
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>The current thread is assumed to hold the lock associated with this
     * {@code Condition} when this method is called.
     * It is up to the implementation to determine if this is
     * the case and if not, how to respond. Typically, an exception will be
     * thrown (such as {@link IllegalMonitorStateException}) and the
     * implementation must document that fact.
     *
     *<p><b>实现注意事项</b> <p>调用此方法时，假定当前线程持有与此`Condition`关联的锁。
     * 确认这一点及其真实性，以及如果不满足这一条件时如何响应，是由具体实现来决定的。
     * 一般情况下，如果发现当前线程没有持有锁，实现可能会抛出异常（例如`{@link IllegalMonitorStateException}`）
     * ，并且实现文档中必须指明这一行为。
     这部分说明强调了在使用诸如awaitNanos(long)这类Condition方法时，调用线程应当已经获得了相应的锁
     。虽然这不是由Java语言本身强制执行的，但大多数Condition的实现会要求这样做，并且如果没有遵守，可能会抛出异常。
     因此，开发者在编写代码时，必须确保在调用这些等待方法之前已经正确地获取了锁，并且应当查阅所使用的具体实现文档，
     了解其确切的要求和行为 。
     *
     */
    void awaitUninterruptibly();

    /**
     * Causes the current thread to wait until it is signalled or interrupted,
     * or the specified waiting time elapses.
     *
     * <p>The lock associated with this condition is atomically
     * released and the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until <em>one</em> of five things happens:
     * <ul>
     * <li>Some other thread invokes the {@link #signal} method for this
     * {@code Condition} and the current thread happens to be chosen as the
     * thread to be awakened; or
     * <li>Some other thread invokes the {@link #signalAll} method for this
     * {@code Condition}; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of thread suspension is supported; or
     * <li>The specified waiting time elapses; or
     * <li>A &quot;<em>spurious wakeup</em>&quot; occurs.
     * </ul>
     *
     * <p>In all cases, before this method can return the current thread must
     * re-acquire the lock associated with this condition. When the
     * thread returns it is <em>guaranteed</em> to hold this lock.
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while waiting
     * and interruption of thread suspension is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared. It is not specified, in the first
     * case, whether or not the test for interruption occurs before the lock
     * is released.
     *
     * <p>The method returns an estimate of the number of nanoseconds
     * remaining to wait given the supplied {@code nanosTimeout}
     * value upon return, or a value less than or equal to zero if it
     * timed out. This value can be used to determine whether and how
     * long to re-wait in cases where the wait returns but an awaited
     * condition still does not hold. Typical uses of this method take
     * the following form:
     *
     *  <pre> {@code
     * boolean aMethod(long timeout, TimeUnit unit) {
     *   long nanos = unit.toNanos(timeout);
     *   lock.lock();
     *   try {
     *     while (!conditionBeingWaitedFor()) {
     *       if (nanos <= 0L)
     *         return false;
     *       nanos = theCondition.awaitNanos(nanos);
     *     }
     *     // ...
     *   } finally {
     *     lock.unlock();
     *   }
     * }}</pre>
     *
     * <p>Design note: This method requires a nanosecond argument so
     * as to avoid truncation errors in reporting remaining times.
     * Such precision loss would make it difficult for programmers to
     * ensure that total waiting times are not systematically shorter
     * than specified when re-waits occur.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>The current thread is assumed to hold the lock associated with this
     * {@code Condition} when this method is called.
     * It is up to the implementation to determine if this is
     * the case and if not, how to respond. Typically, an exception will be
     * thrown (such as {@link IllegalMonitorStateException}) and the
     * implementation must document that fact.
     *
     * <p>An implementation can favor responding to an interrupt over normal
     * method return in response to a signal, or over indicating the elapse
     * of the specified waiting time. In either case the implementation
     * must ensure that the signal is redirected to another waiting thread, if
     * there is one.
     *
     * @param nanosTimeout the maximum time to wait, in nanoseconds
     * @return an estimate of the {@code nanosTimeout} value minus
     *         the time spent waiting upon return from this method.
     *         A positive value may be used as the argument to a
     *         subsequent call to this method to finish waiting out
     *         the desired time.  A value less than or equal to zero
     *         indicates that no time remains.
     * @throws InterruptedException if the current thread is interrupted
     *         (and interruption of thread suspension is supported)
     */
    long awaitNanos(long nanosTimeout) throws InterruptedException;

    /**
     * Causes the current thread to wait until it is signalled or interrupted,
     * or the specified waiting time elapses. This method is behaviorally
     * equivalent to:
     *  <pre> {@code awaitNanos(unit.toNanos(time)) > 0}</pre>
     *
     * @param time the maximum time to wait
     * @param unit the time unit of the {@code time} argument
     * @return {@code false} if the waiting time detectably elapsed
     *         before return from the method, else {@code true}
     * @throws InterruptedException if the current thread is interrupted
     *         (and interruption of thread suspension is supported)
     */
    boolean await(long time, TimeUnit unit) throws InterruptedException;

    /**
     * Causes the current thread to wait until it is signalled or interrupted,
     * or the specified deadline elapses.
     *
     * <p>The lock associated with this condition is atomically
     * released and the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until <em>one</em> of five things happens:
     * <ul>
     * <li>Some other thread invokes the {@link #signal} method for this
     * {@code Condition} and the current thread happens to be chosen as the
     * thread to be awakened; or
     * <li>Some other thread invokes the {@link #signalAll} method for this
     * {@code Condition}; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of thread suspension is supported; or
     * <li>The specified deadline elapses; or
     * <li>A &quot;<em>spurious wakeup</em>&quot; occurs.
     * </ul>
     *
     * <p>In all cases, before this method can return the current thread must
     * re-acquire the lock associated with this condition. When the
     * thread returns it is <em>guaranteed</em> to hold this lock.
     *
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while waiting
     * and interruption of thread suspension is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared. It is not specified, in the first
     * case, whether or not the test for interruption occurs before the lock
     * is released.
     *
     *
     * <p>The return value indicates whether the deadline has elapsed,
     * which can be used as follows:
     *  <pre> {@code
     * boolean aMethod(Date deadline) {
     *   boolean stillWaiting = true;
     *   lock.lock();
     *   try {
     *     while (!conditionBeingWaitedFor()) {
     *       if (!stillWaiting)
     *         return false;
     *       stillWaiting = theCondition.awaitUntil(deadline);
     *     }
     *     // ...
     *   } finally {
     *     lock.unlock();
     *   }
     * }}</pre>
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>The current thread is assumed to hold the lock associated with this
     * {@code Condition} when this method is called.
     * It is up to the implementation to determine if this is
     * the case and if not, how to respond. Typically, an exception will be
     * thrown (such as {@link IllegalMonitorStateException}) and the
     * implementation must document that fact.
     *
     * <p>An implementation can favor responding to an interrupt over normal
     * method return in response to a signal, or over indicating the passing
     * of the specified deadline. In either case the implementation
     * must ensure that the signal is redirected to another waiting thread, if
     * there is one.
     *
     * @param deadline the absolute time to wait until
     * @return {@code false} if the deadline has elapsed upon return, else
     *         {@code true}
     * @throws InterruptedException if the current thread is interrupted
     *         (and interruption of thread suspension is supported)
     */
    boolean awaitUntil(Date deadline) throws InterruptedException;

    /**
     * Wakes up one waiting thread.
     *
     * <p>If any threads are waiting on this condition then one
     * is selected for waking up. That thread must then re-acquire the
     * lock before returning from {@code await}.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>An implementation may (and typically does) require that the
     * current thread hold the lock associated with this {@code
     * Condition} when this method is called. Implementations must
     * document this precondition and any actions taken if the lock is
     * not held. Typically, an exception such as {@link
     * IllegalMonitorStateException} will be thrown.
     */
    void signal();

    /**
     * Wakes up all waiting threads.
     *
     * <p>If any threads are waiting on this condition then they are
     * all woken up. Each thread must re-acquire the lock before it can
     * return from {@code await}.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>An implementation may (and typically does) require that the
     * current thread hold the lock associated with this {@code
     * Condition} when this method is called. Implementations must
     * document this precondition and any actions taken if the lock is
     * not held. Typically, an exception such as {@link
     * IllegalMonitorStateException} will be thrown.
     */
    void signalAll();
}
