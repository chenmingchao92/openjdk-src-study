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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import sun.misc.Unsafe;

/**
 * Provides a framework for implementing blocking locks and related
 * synchronizers (semaphores, events, etc) that rely on
 * first-in-first-out (FIFO) wait queues.  This class is designed to
 * be a useful basis for most kinds of synchronizers that rely on a
 * single atomic {@code int} value to represent state. Subclasses
 * must define the protected methods that change this state, and which
 * define what that state means in terms of this object being acquired
 * or released.  Given these, the other methods in this class carry
 * out all queuing and blocking mechanics. Subclasses can maintain
 * other state fields, but only the atomically updated {@code int}
 * value manipulated using methods {@link #getState}, {@link
 * #setState} and {@link #compareAndSetState} is tracked with respect
 * to synchronization.
 *
 * <p>
 *  这个类（AbstractQueuedSynchronizer，AQS）提供了一个框架，
 *  用于实现依赖先进先出（FIFO）等待队列的阻塞锁及相关同步器（如信号量、事件等）。
 *  设计初衷是作为大多数基于单一原子整型值表示状态的同步器的基础。
 *  子类必须定义受保护的方法来改变这个状态，
 *  并定义这些状态变化在对象被获取或释放时的意义。
 *  一旦这些定义完成，此类中的其他方法将自动处理所有的排队和阻塞机制。
 *  子类可以维护其他状态字段，
 *  但只有通过getState、setState和compareAndSetState方法原子更新的整型值会在同步上下文中被跟踪。
 *
 * <p>Subclasses should be defined as non-public internal helper
 * classes that are used to implement the synchronization properties
 * of their enclosing class.  Class
 * {@code AbstractQueuedSynchronizer} does not implement any
 * synchronization interface.  Instead it defines methods such as
 * {@link #acquireInterruptibly} that can be invoked as
 * appropriate by concrete locks and related synchronizers to
 * implement their public methods.

 * <p>
 * 子类应定义为非公开的内部辅助类，用于实现其封闭类的同步属性。
 * AbstractQueuedSynchronizer类本身不实现任何同步接口，
 * 而是定义了如acquireInterruptibly这样的方法，
 * 供具体锁和同步器按需调用以实现它们的公共方法。
 *
 * <p>This class supports either or both a default <em>exclusive</em>
 * mode and a <em>shared</em> mode. When acquired in exclusive mode,
 * attempted acquires by other threads cannot succeed. Shared mode
 * acquires by multiple threads may (but need not) succeed. This class
 * does not &quot;understand&quot; these differences except in the
 * mechanical sense that when a shared mode acquire succeeds, the next
 * waiting thread (if one exists) must also determine whether it can
 * acquire as well. Threads waiting in the different modes share the
 * same FIFO queue. Usually, implementation subclasses support only
 * one of these modes, but both can come into play for example in a
 * {@link ReadWriteLock}. Subclasses that support only exclusive or
 * only shared modes need not define the methods supporting the unused mode.
 *
 * <p>
 * 该类支持独占模式和共享模式，或两者兼备。
 * 在独占模式下获取时，其他线程的获取尝试不能成功。
 * 而在共享模式下，多个线程的获取尝试可能（但不必）成功。
 * AQS本身不对这些模式差异进行“理解”，但在机械意义上，当共享模式下的获取成功时，
 * 下一个等待的线程（如果存在）也需要判断自己是否也能获取成功。
 * 不同模式等待的线程共享同一个FIFO队列。
 * 通常，实现子类只支持这两种模式之一，但在如读写锁的情况下，两种模式都可能涉及。
 * 仅支持独占或共享模式的子类不必定义未使用的模式所对应的方法。
 *
 * <p>This class defines a nested {@link ConditionObject} class that
 * can be used as a {@link Condition} implementation by subclasses
 * supporting exclusive mode for which method {@link
 * #isHeldExclusively} reports whether synchronization is exclusively
 * held with respect to the current thread, method {@link #release}
 * invoked with the current {@link #getState} value fully releases
 * this object, and {@link #acquire}, given this saved state value,
 * eventually restores this object to its previous acquired state.  No
 * {@code AbstractQueuedSynchronizer} method otherwise creates such a
 * condition, so if this constraint cannot be met, do not use it.  The
 * behavior of {@link ConditionObject} depends of course on the
 * semantics of its synchronizer implementation.
 *
 * <p>
 * AQS定义了一个嵌套类AbstractQueuedSynchronizer.ConditionObject，
 * 可用作支持独占模式的子类的Condition实现，
 * 前提是isHeldExclusively方法报告当前线程是否独占持有同步状态，
 * 调用release方法并传入当前getState值可以完全释放此对象，而通过保存的状态值调用acquire最终能恢复对象到先前的获取状态。
 * 如果不能满足这些约束，则不应使用它。AbstractQueuedSynchronizer.ConditionObject的行为当然取决于其同步器实现的语义。

 * <p>This class provides inspection, instrumentation, and monitoring
 * methods for the internal queue, as well as similar methods for
 * condition objects. These can be exported as desired into classes
 * using an {@code AbstractQueuedSynchronizer} for their
 * synchronization mechanics.
 *
 * 此类提供了对内部队列以及条件对象的检查、监控方法，可根据需要导出到使用AQS作为同步机制的类中。
 *
 * <p>Serialization of this class stores only the underlying atomic
 * integer maintaining state, so deserialized objects have empty
 * thread queues. Typical subclasses requiring serializability will
 * define a {@code readObject} method that restores this to a known
 * initial state upon deserialization.
 *
 * <p>
 * 此类的序列化仅存储维护状态的底层原子整数，因此反序列化的对象具有空的线程队列。
 * 通常需要可序列化的子类会定义readObject方法，在反序列化时恢复到已知的初始状态。
 *
 * <h3>Usage</h3>
 *
 * <p>To use this class as the basis of a synchronizer, redefine the
 * following methods, as applicable, by inspecting and/or modifying
 * the synchronization state using {@link #getState}, {@link
 * #setState} and/or {@link #compareAndSetState}:
 * * <p>
 *  使用方法
 *  要将此类作为同步器的基础，需要根据需要重定义以下方法，通过使用getState、setState和/或compareAndSetState来检查和/或修改同步状态：
 * <ul>
 * <li> {@link #tryAcquire}
 * <li> {@link #tryRelease}
 * <li> {@link #tryAcquireShared}
 * <li> {@link #tryReleaseShared}
 * <li> {@link #isHeldExclusively}
 * </ul>
 *
 * Each of these methods by default throws {@link
 * UnsupportedOperationException}.  Implementations of these methods
 * must be internally thread-safe, and should in general be short and
 * not block. Defining these methods is the <em>only</em> supported
 * means of using this class. All other methods are declared
 * {@code final} because they cannot be independently varied.

 * 这些方法默认抛出UnsupportedOperationException。
 * 实现这些方法必须内部线程安全，并且通常应简短，不阻塞。
 * 定义这些方法是使用此类的唯一支持方式。所有其他方法都被声明为final，因为它们不能独立变化。

 * <p>You may also find the inherited methods from {@link
 * AbstractOwnableSynchronizer} useful to keep track of the thread
 * owning an exclusive synchronizer.  You are encouraged to use them
 * -- this enables monitoring and diagnostic tools to assist users in
 * determining which threads hold locks.
 *
 *<p>您可能会发现从AbstractOwnableSynchronizer继承的方法对于跟踪拥有独占同步器的线程非常有用。
 * 我们鼓励您使用这些方法——这将使得监控和诊断工具能够帮助用户确定哪些线程持有锁。
 * 这样一来，可以更有效地分析和解决潜在的死锁或资源竞争问题，提升应用程序的可观察性和稳定性

 * <p>Even though this class is based on an internal FIFO queue, it
 * does not automatically enforce FIFO acquisition policies.  The core
 * of exclusive synchronization takes the form:
 * <p>  尽管此类基于内部FIFO队列，但它并不自动强制FIFO获取策略。
 * 独占同步的核心逻辑如下：

 * <pre>
 * Acquire:
 *     while (!tryAcquire(arg)) {
 *        <em>enqueue thread if it is not already queued</em>;
 *        <em>如果线程尚未排队，则将其入队；</em>
 *        <em>possibly block current thread</em>;
 *        <em> 可能阻塞当前线程；</em>
 *     }
 *
 * Release:
 *     if (tryRelease(arg))
 *        <em>unblock the first queued thread</em>;
 *        <em>唤醒队列中的第一个线程；</em>
 * </pre>
 *
 * (Shared mode is similar but may involve cascading signals.)
  * （共享模式类似，但可能涉及级联信号。）
 *
 * <p id="barging">Because checks in acquire are invoked before
 * enqueuing, a newly acquiring thread may <em>barge</em> ahead of
 * others that are blocked and queued.  However, you can, if desired,
 * define {@code tryAcquire} and/or {@code tryAcquireShared} to
 * disable barging by internally invoking one or more of the inspection
 * methods, thereby providing a <em>fair</em> FIFO acquisition order.
 * In particular, most fair synchronizers can define {@code tryAcquire}
 * to return {@code false} if {@link #hasQueuedPredecessors} (a method
 * specifically designed to be used by fair synchronizers) returns
 * {@code true}.  Other variations are possible.

 <p id="barging">由于在入队之前就进行了获取（acquire）检查，新尝试获取同步的线程可能会出现所谓的<em>插队</em>现象，
 即它可能会越过那些已被阻塞并排队等候的其他线程。
 然而，如果您希望的话，可以通过定义`tryAcquire`和/或`tryAcquireShared`方法来内部调用一个或多个检查方法，
 以此来禁用这种插队行为，从而实现一个<em>公平的</em>先进先出（FIFO）获取顺序。
 具体来说，大多数公平的同步器可以通过定义`tryAcquire`方法，
 在`hasQueuedPredecessors`（一个专为公平同步器设计的方法）返回`true`时返回`false`
 以此来实现公平性。当然，还有其他各种实现公平性的方式可供选择。

 * <p>Throughput and scalability are generally highest for the
 * default barging (also known as <em>greedy</em>,
 * <em>renouncement</em>, and <em>convoy-avoidance</em>) strategy.
 * While this is not guaranteed to be fair or starvation-free, earlier
 * queued threads are allowed to recontend before later queued
 * threads, and each recontention has an unbiased chance to succeed
 * against incoming threads.  Also, while acquires do not
 * &quot;spin&quot; in the usual sense, they may perform multiple
 * invocations of {@code tryAcquire} interspersed with other
 * computations before blocking.  This gives most of the benefits of
 * spins when exclusive synchronization is only briefly held, without
 * most of the liabilities when it isn't. If so desired, you can
 * augment this by preceding calls to acquire methods with
 * "fast-path" checks, possibly prechecking {@link #hasContended}
 * and/or {@link #hasQueuedThreads} to only do so if the synchronizer
 * is likely not to be contended.

 <p>默认的插队策略（也称为<em>贪婪</em>、<em>放弃</em>和<em>避免车队效应</em>）通常能提供最高的吞吐量和可伸缩性。
 尽管不能保证绝对的公平性或无饿死现象，但该策略允许较早加入队列的线程在后来的线程之前重新尝试获取锁，
 且每次重新尝试都有与新到达线程平等竞争成功的机会。
 此外，虽然获取锁的操作并不进行典型的“自旋”等待，
 但在实际阻塞之前，它们可能会穿插执行多次`tryAcquire`尝试，并夹杂其他运算。
 这种方式在独占同步状态只会被短暂占用时，能获得类似自旋锁的好处，而又避免了在长时间占用时自旋锁带来的开销。
 如有需要，可以通过在调用获取方法之前增加“快速路径”检查来进一步优化，
 比如预先检查`hasContended`和/或`hasQueuedThreads`，
 仅在同步器很可能未被争用时才执行这些操作，以此提升效率。

 * <p>This class provides an efficient and scalable basis for
 * synchronization in part by specializing its range of use to
 * synchronizers that can rely on {@code int} state, acquire, and
 * release parameters, and an internal FIFO wait queue. When this does
 * not suffice, you can build synchronizers from a lower level using
 * {@link java.util.concurrent.atomic atomic} classes, your own custom
 * {@link java.util.Queue} classes, and {@link LockSupport} blocking
 * support.

 * <p>此类通过专门针对可以依赖于`int`类型的同步状态、获取（acquire）与释放（release）参数，
 * 以及内部先进先出（FIFO）等待队列的同步器，提供了一种高效且可扩展的同步基础。
 * 当这些内置特性不能满足需求时，你可以基于更低层次的组件来构建自定义同步器，
 * 包括使用`java.util.concurrent.atomic`包中的原子类、
 * 自定义的`java.util.Queue`队列类，以及借助`LockSupport`类提供的阻塞支持。

 * <h3>Usage Examples</h3>
 *
 * <p>Here is a non-reentrant mutual exclusion lock class that uses
 * the value zero to represent the unlocked state, and one to
 * represent the locked state. While a non-reentrant lock
 * does not strictly require recording of the current owner
 * thread, this class does so anyway to make usage easier to monitor.
 * It also supports conditions and exposes
 * one of the instrumentation methods:
 * <h3>使用示例</h3>
 * <p>以下是一个非可重入互斥锁类的示例，该类使用值0表示未锁定状态，
 * 使用1表示已锁定状态。尽管非可重入锁在技术上并不严格要求记录当前持有锁的线程，
 * 但本类仍然这么做，以便于监控和调试。此外，它还支持条件等待，并公开了一个用于检测的工具方法：
 *  <pre> {@code
 * class Mutex implements Lock, java.io.Serializable {
 *
 *   // Our internal helper class 用于实现同步的内部辅助类
 *   private static class Sync extends AbstractQueuedSynchronizer {
 *     // Reports whether in locked state 判断是否处于锁定状态
 *     protected boolean isHeldExclusively() {
 *       return getState() == 1;
 *     }
 *
 *     // Acquires the lock if state is zero 尝试获取锁，成功则返回true
 *     public boolean tryAcquire(int acquires) {
 *       assert acquires == 1; // Otherwise unused 除了1以外的值不被使用
 *       if (compareAndSetState(0, 1)) {
 *         setExclusiveOwnerThread(Thread.currentThread());
 *         return true;
 *       }
 *       return false;
 *     }
 *
 *     // Releases the lock by setting state to zero 释放锁，将状态设置为0
 *     protected boolean tryRelease(int releases) {
 *       assert releases == 1; // Otherwise unused 除了1以外的值不被使用
 *       if (getState() == 0) throw new IllegalMonitorStateException();
 *       setExclusiveOwnerThread(null);
 *       setState(0);
 *       return true;
 *     }
 *
 *     // Provides a Condition 提供一个Condition
 *     Condition newCondition() { return new ConditionObject(); }
 *
 *     // Deserializes properly 反序列化
 *     private void readObject(ObjectInputStream s)
 *         throws IOException, ClassNotFoundException {
 *       s.defaultReadObject();
 *       setState(0); // reset to unlocked state 重置为未锁定状态
 *     }
 *   }
 *
 *   // The sync object does all the hard work. We just forward to it. sync对象完成所有的工作，我们只需将请求转发给它
 *   private final Sync sync = new Sync();
 *
 *   public void lock()                { sync.acquire(1); }
 *   public boolean tryLock()          { return sync.tryAcquire(1); }
 *   public void unlock()              { sync.release(1); }
 *   public Condition newCondition()   { return sync.newCondition(); }
 *   public boolean isLocked()         { return sync.isHeldExclusively(); }
 *   public boolean hasQueuedThreads() { return sync.hasQueuedThreads(); }
 *   public void lockInterruptibly() throws InterruptedException {
 *     sync.acquireInterruptibly(1);
 *   }
 *   public boolean tryLock(long timeout, TimeUnit unit)
 *       throws InterruptedException {
 *     return sync.tryAcquireNanos(1, unit.toNanos(timeout));
 *   }
 * }}</pre>
 *
 * <p>Here is a latch class that is like a
 * {@link java.util.concurrent.CountDownLatch CountDownLatch}
 * except that it only requires a single {@code signal} to
 * fire. Because a latch is non-exclusive, it uses the {@code shared}
 * acquire and release methods.
 *  <p>以下是一个类似于CountDownLatch的门闩类，但它只需要一个信号就能触发。
 *  由于门闩是非独占的，因此它使用了共享的获取和释放方法。
 *
 *  <pre> {@code
 * class BooleanLatch {
 *
 *   private static class Sync extends AbstractQueuedSynchronizer {
 *     boolean isSignalled() { return getState() != 0; }
 *
 *     protected int tryAcquireShared(int ignore) {
 *       return isSignalled() ? 1 : -1;
 *     }
 *
 *     protected boolean tryReleaseShared(int ignore) {
 *       setState(1);
 *       return true;
 *     }
 *   }
 *
 *   private final Sync sync = new Sync();
 *   public boolean isSignalled() { return sync.isSignalled(); }
 *   public void signal()         { sync.releaseShared(1); }
 *   public void await() throws InterruptedException {
 *     sync.acquireSharedInterruptibly(1);
 *   }
 * }}</pre>
 *
 * @since 1.5
 * @author Doug Lea
 */
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {

    private static final long serialVersionUID = 7373984972572414691L;

    /**
     * Creates a new {@code AbstractQueuedSynchronizer} instance
     * with initial synchronization state of zero.
     *
     * 创建一个新的AbstractQueuedSynchronizer实例，初始同步状态为0。
     */
    protected AbstractQueuedSynchronizer() { }

    /**
     * Wait queue node class.
     *
     * <p>The wait queue is a variant of a "CLH" (Craig, Landin, and
     * Hagersten) lock queue. CLH locks are normally used for
     * spinlocks.  We instead use them for blocking synchronizers, but
     * use the same basic tactic of holding some of the control
     * information about a thread in the predecessor of its node.  A
     * "status" field in each node keeps track of whether a thread
     * should block.  A node is signalled when its predecessor
     * releases.  Each node of the queue otherwise serves as a
     * specific-notification-style monitor holding a single waiting
     * thread. The status field does NOT control whether threads are
     * granted locks etc though.  A thread may try to acquire if it is
     * first in the queue. But being first does not guarantee success;
     * it only gives the right to contend.  So the currently released
     * contender thread may need to rewait.
     * <p> 等待队列节点类。
     * <p>此等待队列是“CLH”（Craig、Landin 和 Hagersten）锁队列的一种变体。
     * CLH锁通常应用于自旋锁。而我们将其应用于阻塞同步器，但仍然采用相同的基本策略，
     * 即在节点的前驱节点中保存关于线程的部分控制信息。
     * 每个节点中的“状态”字段跟踪线程是否应该阻塞。
     * 当一个节点的前驱节点释放时，该节点会被唤醒。
     * 队列中的每个节点除此外还充当一种特定通知风格的监视器，
     * 每个监视器只持有单一的等待线程。状态字段并不控制线程是否获得锁等权限，
     * 尽管一个线程如果位于队列首位可以尝试获取，但首位并不能保证成功，
     * 它仅仅赋予了竞争的权利。因此，当前被释放的竞争线程可能还需重新等待。
     * <p>To enqueue into a CLH lock, you atomically splice it in as new
     * tail. To dequeue, you just set the head field.
     *
     * <pre>
     *      +------+  prev +-----+       +-----+
     * head |      | <---- |     | <---- |     |  tail
     *      +------+       +-----+       +-----+
     * </pre>
     *
     * <p>Insertion into a CLH queue requires only a single atomic
     * operation on "tail", so there is a simple atomic point of
     * demarcation from unqueued to queued. Similarly, dequeuing
     * involves only updating the "head". However, it takes a bit
     * more work for nodes to determine who their successors are,
     * in part to deal with possible cancellation due to timeouts
     * and interrupts.
     * <p>
     * 向CLH锁队列中入队仅需对"尾部"进行一次原子操作，因此从未入队到已入队状态有一个简单的原子区分点。
     * 相应地，出队仅涉及更新"头部"。然而，节点确定其后继者需要更多工作，
     * 部分原因是为了处理因超时和中断可能导致的取消情况。
     *
     * <p>The "prev" links (not used in original CLH locks), are mainly
     * needed to handle cancellation. If a node is cancelled, its
     * successor is (normally) relinked to a non-cancelled
     * predecessor. For explanation of similar mechanics in the case
     * of spin locks, see the papers by Scott and Scherer at
     * <a href="http://www.cs.rochester.edu/u/scott/synchronization/">...</a>
     * <p>
     * “前驱”链接（非CLH锁原始设计中所用），主要用于处理取消操作。
     * 如果一个节点被取消，其后继节点通常会被重新链接到一个未被取消的前驱节点上。
     * 关于自旋锁中类似机制的解释，
     * 请参考Scott和Scherer在http://www.cs.rochester.edu/u/scott/synchronization/上发表的论文。
     *
     * <p>We also use "next" links to implement blocking mechanics.
     * The thread id for each node is kept in its own node, so a
     * predecessor signals the next node to wake up by traversing
     * next link to determine which thread it is.  Determination of
     * successor must avoid races with newly queued nodes to set
     * the "next" fields of their predecessors.  This is solved
     * when necessary by checking backwards from the atomically
     * updated "tail" when a node's successor appears to be null.
     * (Or, said differently, the next-links are an optimization
     * so that we don't usually need a backward scan.)
     *
     *
     * <p>
     * 我们也利用“后继”链接来实现阻塞机制。每个节点存储自己的线程ID，因此前驱节点通过遍历后继链接来确定并唤醒下一个线程。
     * 确定后继节点时必须避免与新入队节点设置其前驱的"后继"字段时产生的竞争。
     * 如有必要，当一个节点的后继看似为null时，通过从原子更新的"尾部"反向检查来解决这一问题。
     * 换言之，“后继”链接是一种优化，使我们通常不需要进行反向扫描。
     *
     * <p>Cancellation introduces some conservatism to the basic
     * algorithms.  Since we must poll for cancellation of other
     * nodes, we can miss noticing whether a cancelled node is
     * ahead or behind us. This is dealt with by always unparking
     * successors upon cancellation, allowing them to stabilize on
     * a new predecessor, unless we can identify an uncancelled
     * predecessor who will carry this responsibility.
     * <p>
     * 取消机制给基础算法带来了一定程度的保守性。
     * 因为我们必须轮询其他节点的取消状态，可能会忽视某个被取消的节点是在我们之前还是之后。
     * 解决办法是，在取消时总是唤醒后继者，使其能稳定在一个新的前驱上，
     * 除非我们能识别出一个未被取消的前驱来承担这项职责。
     * <p>CLH queues need a dummy header node to get started. But
     * we don't create them on construction, because it would be wasted
     * effort if there is never contention. Instead, the node
     * is constructed and head and tail pointers are set upon first
     * contention.
     * <p>
     *CLH队列启动时需要一个虚拟头节点。但我们不在构造时创建它，因为如果没有争用，这样的努力将被浪费。
     * 相反，首次发生争用时，节点被构造，同时设置头和尾指针。
     *
     * <p>Threads waiting on Conditions use the same nodes, but
     * use an additional link. Conditions only need to link nodes
     * in simple (non-concurrent) linked queues because they are
     * only accessed when exclusively held.  Upon await, a node is
     * inserted into a condition queue.  Upon signal, the node is
     * transferred to the main queue.  A special value of status
     * field is used to mark which queue a node is on.
     * <p>
     *     等待条件的线程使用相同的节点，但使用了额外的链接。
     *     条件只需要将节点链接到简单（非并发）的链接队列中，因为它们只在独占持有时才会被访问。
     *     在等待时，一个节点被插入到条件队列中。在接收到信号时，节点被转移到主队列中。
     *     一个特殊的状态字段值用于标记节点所在的队列。
     *

     *
     * <p>Thanks go to Dave Dice, Mark Moir, Victor Luchangco, Bill
     * Scherer and Michael Scott, along with members of JSR-166
     * expert group, for helpful ideas, discussions, and critiques
     * on the design of this class.
     * <p>
     * 感谢Dave Dice、Mark Moir、Victor Luchangco、Bill Scherer、Michael Scott以及JSR-166专家小组的成员，
     * 他们对于此类设计提供了有益的想法、讨论和批判
     */
    static final class Node {
        /** Marker to indicate a node is waiting in shared mode
         * 标记节点正处于共享模式等待中
         * */
        static final Node SHARED = new Node();
        /** Marker to indicate a node is waiting in exclusive mode
         * 标记节点正处于独占模式等待中
         * */
        static final Node EXCLUSIVE = null;

        /** waitStatus value to indicate thread has cancelled
         * 等待状态值，表示线程已取消等待
         * */
        static final int CANCELLED =  1;
        /** waitStatus value to indicate successor's thread needs unparking
         * 等待状态值，表示后继节点的线程需要被唤醒
         * */
        static final int SIGNAL    = -1;
        /** waitStatus value to indicate thread is waiting on condition
         * 等待状态值，表示线程正在等待某个条件
         * */
        static final int CONDITION = -2;
        /**
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         等待状态值，指示下一个共享模式的获取操作应无条件传播
         * （这通常与信号传递相关，确保多个等待线程能够被正确唤醒）
         */
        static final int PROPAGATE = -3;

        /**
         * Status field, taking on only the values:
         *   SIGNAL:     The successor of this node is (or will soon be)
         *               blocked (via park), so the current node must
         *               unpark its successor when it releases or
         *               cancels. To avoid races, acquire methods must
         *               first indicate they need a signal,
         *               then retry the atomic acquire, and then,
         *               on failure, block.
         *   CANCELLED:  This node is cancelled due to timeout or interrupt.
         *               Nodes never leave this state. In particular,
         *               a thread with cancelled node never again blocks.
         *   CONDITION:  This node is currently on a condition queue.
         *               It will not be used as a sync queue node
         *               until transferred, at which time the status
         *               will be set to 0. (Use of this value here has
         *               nothing to do with the other uses of the
         *               field, but simplifies mechanics.)
         *   PROPAGATE:  A releaseShared should be propagated to other
         *               nodes. This is set (for head node only) in
         *               doReleaseShared to ensure propagation
         *               continues, even if other operations have
         *               since intervened.
         *   0:          None of the above
         *
         * The values are arranged numerically to simplify use.
         * Non-negative values mean that a node doesn't need to
         * signal. So, most code doesn't need to check for particular
         * values, just for sign.
         *
         * The field is initialized to 0 for normal sync nodes, and
         * CONDITION for condition nodes.  It is modified using CAS
         * (or when possible, unconditional volatile writes).
         */
        /**
         * 状态字段，仅取以下值：
         *   SIGNAL:     当前节点的后继节点（或即将）被阻塞（通过park方法），因此当当前节点释放或取消时，
         *               必须唤醒它的后继节点。为了避免竞态条件，获取方法必须首先表明它们需要信号，
         *               然后重试原子获取操作，失败后则进行阻塞。
         *   CANCELLED:  该节点因超时或中断而被取消。节点一旦进入此状态便不再改变。
         *               特别地，拥有已取消节点的线程将不再阻塞。
         *   CONDITION:  该节点当前位于条件队列中。
         *               在转移之前，它不会作为同步队列节点使用，转移时状态将被设置为0。
         *               （此处使用此值与其他用途无关，但简化了机制。）
         *   PROPAGATE:  共享释放操作应传播给其他节点。仅在头节点上设置此标志（在doReleaseShared方法中），
         *               确保即使有其他操作介入，传播也能继续进行。
         *   0:          上述都不是
         *
         * 这些值按数值顺序排列以简化使用。
         * 非负值表示节点不需要发送信号。因此，大多数代码不需要检查特定值，只需检查符号（正负）。
         *
         * 该字段初始化为0，表示普通同步节点，而对于条件节点则初始化为CONDITION。
         * 它通过CAS（或在可能的情况下，无条件的volatile写）进行修改。
         */

        volatile int waitStatus;

        /**
         * Link to predecessor node that current node/thread relies on
         * for checking waitStatus. Assigned during enqueuing, and nulled
         * out (for sake of GC) only upon dequeuing.  Also, upon
         * cancellation of a predecessor, we short-circuit while
         * finding a non-cancelled one, which will always exist
         * because the head node is never cancelled: A node becomes
         * head only as a result of successful acquire. A
         * cancelled thread never succeeds in acquiring, and a thread only
         * cancels itself, not any other node.
         */
        /**
         * 指向当前节点/线程依赖的前驱节点，用于检查等待状态(waitStatus)。
         * 入队时分配，并在出队时置空（为了GC回收）。当遇到前驱节点被取消的情况时，
         * 我们会跳过这些节点去寻找一个未被取消的前驱节点，这样的节点总是存在的，
         * 因为头节点永远不会被取消：一个节点只有在成功获取到同步状态时才能成为头节点。
         * 被取消的线程永远无法成功获取到锁，而且线程只会取消自己，不会取消其它节点。
         */
        volatile Node prev;

        /**
         * Link to the successor node that the current node/thread
         * unparks upon release. Assigned during enqueuing, adjusted
         * when bypassing cancelled predecessors, and nulled out (for
         * sake of GC) when dequeued.  The enq operation does not
         * assign next field of a predecessor until after attachment,
         * so seeing a null next field does not necessarily mean that
         * node is at end of queue. However, if a next field appears
         * to be null, we can scan prev's from the tail to
         * double-check.  The next field of cancelled nodes is set to
         * point to the node itself instead of null, to make life
         * easier for isOnSyncQueue.
         */
        /**
         * 指向当前节点/线程释放时需要唤醒的后继节点。
         * 入队时分配，当绕过被取消的前驱节点时会进行调整，并在出队时置空（同样为了GC回收）。
         * 入队操作在连接到前驱节点之后才会设置前驱节点的next字段，因此看到next为null并不一定意味着该节点位于队列的末端。
         * 但是，如果发现next看起来为null，我们可以通过从队列尾部开始反向遍历prev指针来双重确认。
         * 取消的节点的next字段被设置指向自己而非null，这样做使得isOnSyncQueue方法的实现更简单。
         */
        volatile Node next;

        /**
         * The thread that enqueued this node.  Initialized on
         * construction and nulled out after use.
         */
        /**
         * 指向封装该节点的线程。
         * 初始化于构建时，并在使用完毕后置空。
         */
        volatile Thread thread;

        /**
         * 指向等待在条件上的下一个节点，或者使用特殊值SHARED来标记。
         * 由于条件队列只在持有独占锁的情况下访问，所以我们只需要一个简单的链表来暂存等待条件的节点。
         * 当这些节点等待的条件满足时，它们会被转移回同步队列以重新尝试获取锁。
         * 并且，因为条件等待总是独占的，我们可以利用特殊值来表示共享模式，这样就可以省去一个字段。
         */
        Node nextWaiter;

        /**
         * Returns true if node is waiting in shared mode.
         * 如果节点正在以共享模式等待，则返回true。
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * Returns previous node, or throws NullPointerException if null.
         * Use when predecessor cannot be null.  The null check could
         * be elided, but is present to help the VM.
         *
         * @return the predecessor of this node
         */
        /**
         * 返回前驱节点，如果为null则抛出NullPointerException。
         * 仅在确定前驱节点不可能为null时使用。虽然null检查可以省略，
         * 但保留它有助于虚拟机进行优化。
         *
         * @return 此节点的前驱节点
         */
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }

    /**
     * Head of the wait queue, lazily initialized.  Except for
     * initialization, it is modified only via method setHead.  Note:
     * If head exists, its waitStatus is guaranteed not to be
     * CANCELLED.
     */
    /**
     * 等待队列的头节点，惰性初始化。除了初始化过程外，仅通过setHead方法进行修改。
     * 注意：如果头节点存在，其waitStatus状态保证不是CANCELLED（已取消）。
     */
    private transient volatile Node head;

    /**
     * Tail of the wait queue, lazily initialized.  Modified only via
     * method enq to add new wait node.
     */
    /**
     * 等待队列的尾节点，惰性初始化。仅通过enq方法添加新等待节点时进行修改。
     */
    private transient volatile Node tail;

    /**
     * The synchronization state.
     */
    /**
     * 同步状态。
     */
    private volatile int state;

    /**
     * Returns the current value of synchronization state.
     * This operation has memory semantics of a {@code volatile} read.
     * @return current state value
     */
    /**
     * 返回当前同步状态的值。此操作具有volatile读取的内存语义。
     * @return 当前状态值
     */
    protected final int getState() {
        return state;
    }

    /**
     * Sets the value of synchronization state.
     * This operation has memory semantics of a {@code volatile} write.
     * @param newState the new state value
     */
    /**
     * 设置同步状态的新值。此操作具有volatile写入的内存语义。
     * @param newState 新的状态值
     */
    protected final void setState(int newState) {
        state = newState;
    }

    /**
     * Atomically sets synchronization state to the given updated
     * value if the current state value equals the expected value.
     * This operation has memory semantics of a {@code volatile} read
     * and write.
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that the actual
     *         value was not equal to the expected value.
     */
    /**
     * 如果当前状态值等于期望值，则原子地将同步状态设置为给定的更新值。
     * 此操作具有volatile读和写的内存语义。
     *
     * @param expect 期望值
     * @param update 新值
     * @return 如果操作成功则返回{@code true}。如果返回false，表示实际值并不等于期望值。
     */
    protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    // Queuing utilities

    /**
     * The number of nanoseconds for which it is faster to spin
     * rather than to use timed park. A rough estimate suffices
     * to improve responsiveness with very short timeouts.
     */
    /**
     * 当超时很短时，相较于使用带超时的挂起操作（timed park），
     * 自旋（spin）等待更优的纳秒数阈值。一个粗略的估计就足以提升对极短超时的响应速度。
     */
    static final long spinForTimeoutThreshold = 1000L;

    /**
     * Inserts node into queue, initializing if necessary. See picture above.
     * @param node the node to insert
     * @return node's predecessor
     */
    /**
     * 将节点插入队列中，并在必要时进行初始化。参见上文的队列示意图。
     * @param node 要插入的节点
     * @return 节点的前驱节点
     */
    private Node enq(final Node node) {
        // 使用无限循环确保操作成功,如果入队失败，也就是有其他节点抢占了入队，贼再次循环
        for (;;) {
            Node t = tail;  // 获取当前队列的尾节点
            if (t == null) { // Must initialize // 如果队列尚未初始化（尾节点为null）
                // 尝试通过CAS操作设置一个新的头节点，作为初始化队列的第一步
                if (compareAndSetHead(new Node()))
                    // 初始化成功后，将尾节点也指向这个新节点
                    tail = head;
            } else {
                // 队列已经初始化，常规入队操作
                node.prev = t;  // 设置新节点的前驱为当前尾节点
                // 使用CAS操作尝试将新节点设置为新的尾节点
                if (compareAndSetTail(t, node)) {
                    // 如果CAS成功，将原尾节点的next指针指向新节点，完成入队
                    t.next = node;
                    // 返回新节点的前驱节点
                    return t;
                }
            }
        }
    }

    /**
     * Creates and enqueues node for current thread and given mode.
     *
     * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
     * @return the new node
     */
    /**
     * 为当前线程和指定模式创建并入队一个新的节点。
     *
     * @param mode 节点模式，Node.EXCLUSIVE 表示独占模式，Node.SHARED 表示共享模式
     * @return 创建的新节点
     */
    private Node addWaiter(Node mode) {
        // 为当前线程和给定模式创建一个新的节点 //独享模式或共享模式
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        // 尝试快速入队路径：如果队列的尾节点已知（非空），则直接将新节点插入队列尾部
        Node pred = tail;
        if (pred != null) {
            // 设置新节点的前置节点为当前尾节点
            node.prev = pred;
            // 使用CAS操作尝试将队列尾节点更新为新节点，如果成功，则完成了快速入队
            if (compareAndSetTail(pred, node)) {
                // 确保原尾节点的next指针指向新节点，保持链表的完整性
                pred.next = node;
                // 入队成功，返回新节点
                return node;
            }
        }
        // 快速入队失败，调用enq方法进行全步骤的入队操作
        enq(node);
        return node;
    }

    /**
     * Sets head of queue to be node, thus dequeuing. Called only by
     * acquire methods.  Also nulls out unused fields for sake of GC
     * and to suppress unnecessary signals and traversals.
     *
     * @param node the node
     */
    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    /**
     * Wakes up node's successor, if one exists.
     *
     * @param node the node
     */
    /**
     * 唤醒节点的后继节点中的线程，如果存在的话。
     * 注意这里只唤醒了一个线程
     * @param node 被操作的节点
     */
    private void unparkSuccessor(Node node) { //这里入参是head节点
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         */
        /**
         *    // 如果节点的状态为负（可能需要发出信号），尝试将其清零以预期发出信号。
         *     // 即便此操作失败或等待线程改变了状态也是允许的。因为这里及时失败了，也不会对系统造成很大的影响。
         */
        int ws = node.waitStatus;
        if (ws < 0)
            compareAndSetWaitStatus(node, ws, 0);

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
        /**
         *   // 要唤醒的线程保存在后继节点中，通常就是下一个节点。
         *     // 但如果后继节点被取消或看似为null，则需要从队列尾部开始向前遍历，
         *     // 找到实际上未被取消的后继节点。
         */
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            /* 从尾部向前来遍历，确保找到有效的未取消节点，之所以是在尾部，是因为存在一种情况，如果是新进节点，pre.next还没有正确设置
             这里在 acquire的代码上可以看见。 这里也可以解决，第一个节点正处于取消状态的情况。*/
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        // 如果找到了有效的后继节点，则唤醒它的线程
        if (s != null)
            /*这里唤醒后，就会进入acquireQueued方法，去尝试获取锁*/
            LockSupport.unpark(s.thread);
    }

    /**
     * Release action for shared mode -- signals successor and ensures
     * propagation. (Note: For exclusive mode, release just amounts
     * to calling unparkSuccessor of head if it needs signal.)
     */
    /**
     * 共享模式下的释放动作——通知后继节点并确保传播。
     * （注意：对于独占模式，释放操作仅需在头节点需要信号时调用其unparkSuccessor即可。）
     */
    private void doReleaseShared() {
        /*
         * Ensure that a release propagates, even if there are other
         * in-progress acquires/releases.  This proceeds in the usual
         * way of trying to unparkSuccessor of head if it needs
         * signal. But if it does not, status is set to PROPAGATE to
         * ensure that upon release, propagation continues.
         * Additionally, we must loop in case a new node is added
         * while we are doing this. Also, unlike other uses of
         * unparkSuccessor, we need to know if CAS to reset status
         * fails, if so rechecking.
         */
        /*
         * 确保释放操作得以传播，即便有其他正在进行的获取或释放操作。
         * 此过程常规上尝试唤醒头节点的后继节点（如果需要信号）。但如果不需要信号，
         * 则将状态设置为PROPAGATE，确保释放时传播继续进行。
         * 另外，我们必须循环以防在操作过程中有新节点加入队列。此外，与unparkSuccessor的其他使用不同，
         * 我们需要知道CAS重置状态失败的情况，如果失败则需要重新检查。
         */
        for (;;) {
            Node h = head;
            if (h != null && h != tail) { // 确保队列非空且头节点不是尾节点（队列非空闲）
                int ws = h.waitStatus;  // 获取头节点的等待状态
                if (ws == Node.SIGNAL) { // 如果状态为SIGNAL，表示后继节点需要被唤醒
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))  // 尝试原子地将状态从SIGNAL设置为0
                        continue;             // loop to recheck cases  // 如果CAS失败（可能有其他线程修改了状态），则重新循环检查
                    unparkSuccessor(h);// 唤醒头节点的后继节点
                }
                else if (ws == 0 &&
                         !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))// 如果状态为0，尝试设置状态为PROPAGATE以确保传播
                    continue;                // loop on failed CAS  // 如果CAS失败，重新循环
            } // 注意：这里设置PROPAGATE是为了确保在当前释放操作完成后，下一次释放也能继续传播唤醒
            if (h == head)                   // loop if head changed // 如果在循环体内头节点没有变更，则跳出循环
                break;
        }
    }

    /**
     * Sets head of queue, and checks if successor may be waiting
     * in shared mode, if so propagating if either propagate > 0 or
     * PROPAGATE status was set.
     *
     * @param node the node
     * @param propagate the return value from a tryAcquireShared
     */
    /**
     * 设置队列的头节点，并检查后继节点是否可能处于共享等待状态。
     * 如果满足传播条件（由调用者指示或先前操作记录），
     * 且后继节点处于共享等待状态或未知（因为空时默认视为共享），
     * 则进行信号传播。
     *
     * @param node 需要设置为头节点的节点
     * @param propagate 来自tryAcquireShared方法的返回值，指示是否需要传播
     */
    private void setHeadAndPropagate(Node node, int propagate) { //这里的propagate的值大于0，表示可以继续传播，有足够的许可
        Node h = head; // Record old head for check below // 先记录当前头节点，以便后续检查
        setHead(node); // 设置新节点为头节点
        /*
         * Try to signal next queued node if:
         *   Propagation was indicated by caller,
         *     or was recorded (as h.waitStatus either before
         *     or after setHead) by a previous operation
         *     (note: this uses sign-check of waitStatus because
         *      PROPAGATE status may transition to SIGNAL.)
         * and
         *   The next node is waiting in shared mode,
         *     or we don't know, because it appears null
         *
         * The conservatism in both of these checks may cause
         * unnecessary wake-ups, but only when there are multiple
         * racing acquires/releases, so most need signals now or soon
         * anyway.
         */
        /*
        *  以下条件判断是否尝试唤醒队列中的下一个等待节点：
           1. 明确指示需要传播（propagate > 0）
           2. 前一个头节点的等待状态表明之前有传播需求（waitStatus可能从PROPAGATE转为SIGNAL）
           3. 当前头节点（再次检查以确保最新状态）满足传播条件
           *
           * 这些检查可能引起不必要的唤醒，特别是在并发获取/释放资源时，
           * 但考虑到在高竞争场景下，大多数等待的线程可能很快需要被唤醒，
           * 因此这种保守策略是可以接受的，以确保同步的正确性和及时性。
        * */
        if (propagate > 0 || h == null || h.waitStatus < 0 || /*这里应该算是一种防守型编程吧*/
            (h = head) == null /*这里重新获取了一次头结点，也就是当前的node节点*/ || h.waitStatus < 0) { //这些条件满足其一即可
            Node s = node.next;
            if (s == null || s.isShared()) /*这里如果s==null 后续会被doRelaseShared方法组织掉，如果是共享模式，则继续传播，如果是独占模式，就不会传播这里对独占模式进行了拦截*/
                doReleaseShared();
        }
    }

    // Utilities for various versions of acquire

    /**
     * Cancels an ongoing attempt to acquire.
     *
     *
     * @param node the node
     */
    /**
     * 取消正在进行的获取尝试。
     *
     * @param node 要取消的节点
     */
    private void cancelAcquire(Node node) {
        // Ignore if node doesn't exist
        // 如果节点不存在则忽略
        if (node == null)
            return;
        // 将节点的线程引用置为null，表明该节点不再代表任何线程
        node.thread = null;

        // Skip cancelled predecessors
        // 跳过已取消的前驱节点
        Node pred = node.prev;
        while (pred.waitStatus > 0)
            node.prev = pred = pred.prev;

        // predNext is the apparent node to unsplice. CASes below will
        // fail if not, in which case, we lost race vs another cancel
        // or signal, so no further action is necessary.
        // predNext是看似要断开连接的节点。如果下边的CAS操作失败，
        // 则意味着我们在与另一个取消或信号的竞争中失败了，
        // 因此不需要进一步的操作。
        Node predNext = pred.next;

        // Can use unconditional write instead of CAS here.
        // After this atomic step, other Nodes can skip past us.
        // Before, we are free of interference from other threads.
        // 可以在这里使用无条件写入，而不是CAS操作。
        // 在这个原子步骤之后，其他节点可以跳过我们。
        // 在此之前，我们不受其他线程的干扰。
        node.waitStatus = Node.CANCELLED;
        /* 笔者注: 这里可以先考虑一个正常的中间节点的情况，然后再考虑正常尾部节点的情况，再考虑正常的第一个节点的情况
        *   然后考虑处于尾节点，但是又有线程进入队列的情况， 这时候，我们当前节点就会变成一个孤儿节点，
        * 如果取消的是第一个节点，那么 relase的流程中，倒序寻最前的可用的节点的操作，也保证不会有问题。
        * */
        // If we are the tail, remove ourselves.
        // 如果我们是尾节点，就移除自己
        if (node == tail && compareAndSetTail(node, pred)) {
            // 试图将pred的next指针设置为null，以移除node
            compareAndSetNext(pred, predNext, null);
        } else {
            // 如果后继节点需要唤醒，尝试设置pred的next为后继节点，否则直接唤醒后继
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            int ws;
            if (pred != head &&
                ((ws = pred.waitStatus) == Node.SIGNAL /*这里考虑如果pred此时也变成了CANCELED的情况，如果没有这个判断就会出问题*/ ||
                 (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
                pred.thread != null) {
                Node next = node.next;
                if (next != null && next.waitStatus <= 0) //这里也是判断如果node.next被取消怎么办。
                    // 尝试将pred的next指针指向node的后继节点next，帮助传播唤醒信号
                    compareAndSetNext(pred, predNext, next);
            } else {
                // 直接唤醒后继节点
                /*笔者注： 1.这里假如取消的是第一个节点，就会走到这一步,2.如果pred线程被取消，也会走到这一步。
                * 我感觉这里 写的有点偷懒，因为如果是情况2，没必要进行唤醒，只不过这个unparkSuccess操作后，可以取消掉所有的CANCEL节点操作，
                * 集合acquire方法中的动作。*/
                unparkSuccessor(node);
            }
            // 帮助垃圾回收，将node的next指针指向自己
            node.next = node; // help GC
        }
    }

    /**
     * Checks and updates status for a node that failed to acquire.
     * Returns true if thread should block. This is the main signal
     * control in all acquire loops.  Requires that pred == node.prev.
     *
     * @param pred node's predecessor holding status
     * @param node the node
     * @return {@code true} if thread should block
     */
    /**
     * 检查并更新未成功获取资源的节点状态。
     * 如果线程应当阻塞，则返回true。这是所有获取循环中的主要信号控制点。
     * 需要保证pred节点是node节点的直接前驱。
     *
     * @param pred 持有状态的node的前驱节点
     * @param node 当前检查的节点
     * @return 如果线程应该阻塞，则返回{@code true}
     */
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;  // 获取前驱节点的等待状态
        if (ws == Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             * 这个节点已经设置了状态，要求释放信号来唤醒它（唤醒当前节点），因此它可以安全地挂起。
             */

            return true;
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             *  前驱节点已被取消。跳过前驱节点并指示重试。知道一个未取消的前驱节点或者head
             */
            do {
                node.prev = pred = pred.prev;  // 移动到未取消的前驱节点
            } while (pred.waitStatus > 0);
            pred.next = node;  // 将当前节点设置为新的前驱节点的后继节点
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             *   前驱节点状态为0或PROPAGATE，需要设置为SIGNAL来请求信号，但还不立即挂起。
             *    调用者需要重试以确保在挂起前确实无法获取资源。
             *  这里也会将head节点的值 设置为SIGNAL，注意即使是共享模式，这里也是设置为SIGNAL
             */
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false; // 当前线程还不需要立即阻塞
    }

    /**
     * Convenience method to interrupt current thread.
     */
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * Convenience method to park and then check if interrupted
     *  便利方法：先挂起线程，然后检查是否被中断
     * @return {@code true} if interrupted
     */
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this); //这一行代码会挂起当前线程，将其置于等待状态，直到其他线程调用 unpark() 方法来唤醒它。
       //这个方法会返回当前线程是否被其他线程触发过中断请求。如果有触发过中断请求，它会返回 true，并且对中断标识进行复位，表示已经响应过了中断请求。
        return Thread.interrupted();
    }

    /*
     * Various flavors of acquire, varying in exclusive/shared and
     * control modes.  Each is mostly the same, but annoyingly
     * different.  Only a little bit of factoring is possible due to
     * interactions of exception mechanics (including ensuring that we
     * cancel if tryAcquire throws exception) and other control, at
     * least not without hurting performance too much.
     */

    /**
     * Acquires in exclusive uninterruptible mode for thread already in
     * queue. Used by condition wait methods as well as acquire.
     *
     * @param node the node
     * @param arg the acquire argument
     * @return {@code true} if interrupted while waiting
     */
    /*
     * 提供了多种获取资源的方式，它们在独占/共享模式和控制模式上有所不同。
     * 每种方式大体相似，但因异常处理机制（包括确保当tryAcquire抛出异常时进行取消）
     * 和其它控制逻辑的交互，很难进行大量的抽象化重构，至少在不对性能造成太大影响的前提下难以实现。
     */

    /**
     * 在独占、不可中断模式下，为已在队列中的线程尝试获取资源。
     * 此方法也被条件等待方法使用。
     *
     * @param node 当前线程对应的节点
     * @param arg 尝试获取资源时的参数（例如重入计数）
     * @return 如果在等待过程中被中断，则返回{@code true}
     */
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;  // 初始化为true，表示获取失败，除非成功获取资源或被中断
        try {
            boolean interrupted = false;  // 记录是否被中断
            for (;;) {  // 无限循环直到获取资源或外部中断
                final Node p = node.predecessor();  // 获取当前节点的前驱节点
                if (p == head && tryAcquire(arg)) {  // 如果前驱节点是头节点并且尝试获取资源成功
                    setHead(node);  // 将当前节点设置为头节点
                    p.next = null; // help GC // 帮助垃圾回收前驱节点
                    failed = false;  // 标记获取成功
                    return interrupted; // 返回中断状态，就是获取锁成功后，判断是否发生过中断
                }
                // 判断是否需要挂起当前线程，并执行挂起操作，同时检查是否被中断
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;  // 如果被中断，设置interrupted为true
            }
        } finally {  // 确保即使在发生异常时也能清理资源
            if (failed)  // 如果获取失败（未被中断但也没获取到资源），这里是因为一定会被执行到，所以要做一个条件判断。防止误杀正确的节点。
                cancelAcquire(node);  // 取消该节点的获取操作
        }
    }

    /**
     * Acquires in exclusive interruptible mode.
     * @param arg the acquire argument
     */
    /**
     * 以可中断模式获取独占锁。
     * @param arg 获取操作的参数
     * @throws InterruptedException 如果线程在等待过程中被中断
     */
    private void doAcquireInterruptibly(int arg)
        throws InterruptedException {
        // 将当前线程加入到等待队列的尾部，并标记为独占模式
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt()) /*笔者注：这里如果在排队等待锁的线程，被中断的话，是不会立刻响应的
                    只有在这个线程被唤醒的之后，才会响应*/
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in exclusive timed mode.
     *
     * @param arg the acquire argument
     * @param nanosTimeout max wait time
     * @return {@code true} if acquired
     */
    /**
     * 在带超时的独占模式下尝试获取锁。
     *
     * @param arg 获取操作的参数
     * @param nanosTimeout 最大等待时间，单位为纳秒
     * @return 如果成功获取锁，则返回{@code true}
     * @throws InterruptedException 如果线程在等待过程中被中断
     */
    private boolean doAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        // 如果超时时间已经小于等于0，直接返回失败
        if (nanosTimeout <= 0L)
            return false;
        //定位超时时间
        final long deadline = System.nanoTime() + nanosTimeout;
        //将当前节点加入到等待队列的尾部，并标记为独占模式
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return true;
                }
                /*再做一次超时判断，因为 addWaiter方法和tryAcquire方法不一定会耗时多久
                * 这里也是for循环的终结，因为如果一个线程等待超时，最终会通过这里结束循环，有点隐晦*/
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                    nanosTimeout > spinForTimeoutThreshold)
                    //定时阻塞
                    LockSupport.parkNanos(this, nanosTimeout);
                    //被唤醒或者等待时间到了之后，判断自身是否被中断。
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in shared uninterruptible mode.
     * @param arg the acquire argument
     */
    /**
     * 在不可中断模式下以共享模式获取资源。
     * @param arg 获取操作的参数
     */
    private void doAcquireShared(int arg) {
        //以共享的模式入队
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);// 尝试以共享模式获取资源
                    if (r >= 0) { //获取成功
                        setHeadAndPropagate(node, r);  // 设置当前节点为头节点，并传播唤醒后继节点
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in shared interruptible mode.
     * @param arg the acquire argument
     * 里和doAcquiredShared几乎是一样的，只有一个地方不一样，就是在parkAndCheckInterrupt()方法中，如果线程被中断，就会抛出异常
     */
    private void doAcquireSharedInterruptibly(int arg)
        throws InterruptedException {
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * Acquires in shared timed mode.
     *
     * @param arg the acquire argument
     * @param nanosTimeout max wait time
     * @return {@code true} if acquired
     */
    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return true;
                    }
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                    nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);//自带超时判断
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    // Main exported methods

    /**
     * Attempts to acquire in exclusive mode. This method should query
     * if the state of the object permits it to be acquired in the
     * exclusive mode, and if so to acquire it.
     *
     * <p>This method is always invoked by the thread performing
     * acquire.  If this method reports failure, the acquire method
     * may queue the thread, if it is not already queued, until it is
     * signalled by a release from some other thread. This can be used
     * to implement method {@link Lock#tryLock()}.
     *
     * <p>The default
     * implementation throws {@link UnsupportedOperationException}.
     *
     * @param arg the acquire argument. This value is always the one
     *        passed to an acquire method, or is the value saved on entry
     *        to a condition wait.  The value is otherwise uninterpreted
     *        and can represent anything you like.
     * @return {@code true} if successful. Upon success, this object has
     *         been acquired.
     * @throws IllegalMonitorStateException if acquiring would place this
     *         synchronizer in an illegal state. This exception must be
     *         thrown in a consistent fashion for synchronization to work
     *         correctly.
     * @throws UnsupportedOperationException if exclusive mode is not supported
     */
    /**
     * 尝试以独占模式获取同步状态。此方法应查询对象的状态是否允许其被独占获取，
     * 如果允许，则进行获取。
     *
     * <p>此方法总是由执行获取操作的线程调用。如果此方法报告失败，那么获取方法
     * 可能会将线程排队（如果它尚未排队），直到它被来自其他线程的释放信号所唤醒。
     * 这可用于实现`Lock#tryLock()`方法。
     *
     * <p>默认实现会抛出`UnsupportedOperationException`。
     *
     * @param arg 获取操作的参数。这个值总是传递给获取方法的参数，或者是进入条件等待时保存的值。
     *            该值本身不被解释，可以表示任何你需要的东西。
     * @return 如果成功则返回{@code true}。成功后，此同步器已被获取。
     * @throws IllegalMonitorStateException 如果获取操作会导致此同步器处于非法状态。
     *         为了同步工作正常，此异常必须以一致的方式抛出。
     * @throws UnsupportedOperationException 如果不支持独占模式。
     */
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to set the state to reflect a release in exclusive
     * mode.
     *
     * <p>This method is always invoked by the thread performing release.
     *
     * <p>The default implementation throws
     * {@link UnsupportedOperationException}.
     *
     * @param arg the release argument. This value is always the one
     *        passed to a release method, or the current state value upon
     *        entry to a condition wait.  The value is otherwise
     *        uninterpreted and can represent anything you like.
     * @return {@code true} if this object is now in a fully released
     *         state, so that any waiting threads may attempt to acquire;
     *         and {@code false} otherwise.
     * @throws IllegalMonitorStateException if releasing would place this
     *         synchronizer in an illegal state. This exception must be
     *         thrown in a consistent fashion for synchronization to work
     *         correctly.
     * @throws UnsupportedOperationException if exclusive mode is not supported
     */
    /**
     * 尝试设置状态以反映独占模式下的释放操作。
     *
     * <p>此方法总是由执行释放操作的线程调用。
     *
     * <p>默认实现会抛出`UnsupportedOperationException`。
     *
     * @param arg 释放参数。这个值总是传递给释放方法的参数，或者是进入条件等待时的当前状态值。
     *            该值本身不做特定解读，可以表示你希望的任何内容。
     * @return 如果此对象现在处于完全释放的状态，允许任何等待的线程尝试获取，则返回{@code true}；
     *         否则返回{@code false}。
     * @throws IllegalMonitorStateException 如果释放操作会导致此同步器处于非法状态。
     *         为了确保同步工作的正确性，此异常必须以一致的方式抛出。
     * @throws UnsupportedOperationException 如果不支持独占模式。
     */

    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to acquire in shared mode. This method should query if
     * the state of the object permits it to be acquired in the shared
     * mode, and if so to acquire it.
     *
     * <p>This method is always invoked by the thread performing
     * acquire.  If this method reports failure, the acquire method
     * may queue the thread, if it is not already queued, until it is
     * signalled by a release from some other thread.
     * <p>
     *
     *
     * <p>The default implementation throws {@link
     * UnsupportedOperationException}.
     *
     * @param arg the acquire argument. This value is always the one
     *        passed to an acquire method, or is the value saved on entry
     *        to a condition wait.  The value is otherwise uninterpreted
     *        and can represent anything you like.
     * @return a negative value on failure; zero if acquisition in shared
     *         mode succeeded but no subsequent shared-mode acquire can
     *         succeed; and a positive value if acquisition in shared
     *         mode succeeded and subsequent shared-mode acquires might
     *         also succeed, in which case a subsequent waiting thread
     *         must check availability. (Support for three different
     *         return values enables this method to be used in contexts
     *         where acquires only sometimes act exclusively.)  Upon
     *         success, this object has been acquired.
     * @throws IllegalMonitorStateException if acquiring would place this
     *         synchronizer in an illegal state. This exception must be
     *         thrown in a consistent fashion for synchronization to work
     *         correctly.
     * @throws UnsupportedOperationException if shared mode is not supported
     */
    /**
     * 尝试以共享模式获取。此方法应查询对象的状态是否允许
     * 以共享模式被获取，如果允许，则进行获取。
     *
     * <p>此方法总是由执行获取操作的线程调用。
     * 如果此方法报告失败，获取方法可能会将线程排队（如果它还没有在队列中），
     * 直到其他线程发出释放信号为止。
     *
     * <p>默认实现会抛出{@link UnsupportedOperationException}异常。
     *
     * @param arg 获取参数。这个值总是传递给获取方法的值，
     *        或者是在进入条件等待时保存的值。该值没有其他解释，
     *        并且可以代表任何你喜欢的东西。
     * @return 失败时返回负值；如果共享模式下的获取成功但后续的共享模式获取
     *         无法成功，则返回零；如果共享模式下的获取成功并且后续的共享模式获取
     *         也可能成功，则返回正值，在这种情况下，后续等待的线程
     *         必须检查可用性。（支持三种不同的返回值使得此方法可以在
     *         只有有时才独占地行动的上下文中使用。）成功后，此对象已被获取。
     * @throws IllegalMonitorStateException 如果获取会使这个同步器处于非法状态。
     *         必须以一致的方式抛出此异常，以便同步能够正确工作。
     * @throws UnsupportedOperationException 如果不支持共享模式
     */
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to set the state to reflect a release in shared mode.
     *
     * <p>This method is always invoked by the thread performing release.
     *
     * <p>The default implementation throws
     * {@link UnsupportedOperationException}.
     *
     * @param arg the release argument. This value is always the one
     *        passed to a release method, or the current state value upon
     *        entry to a condition wait.  The value is otherwise
     *        uninterpreted and can represent anything you like.
     * @return {@code true} if this release of shared mode may permit a
     *         waiting acquire (shared or exclusive) to succeed; and
     *         {@code false} otherwise
     * @throws IllegalMonitorStateException if releasing would place this
     *         synchronizer in an illegal state. This exception must be
     *         thrown in a consistent fashion for synchronization to work
     *         correctly.
     * @throws UnsupportedOperationException if shared mode is not supported
     */
    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns {@code true} if synchronization is held exclusively with
     * respect to the current (calling) thread.  This method is invoked
     * upon each call to a non-waiting {@link ConditionObject} method.
     * (Waiting methods instead invoke {@link #release}.)
     *
     * <p>The default implementation throws {@link
     * UnsupportedOperationException}. This method is invoked
     * internally only within {@link ConditionObject} methods, so need
     * not be defined if conditions are not used.
     *
     * @return {@code true} if synchronization is held exclusively;
     *         {@code false} otherwise
     * @throws UnsupportedOperationException if conditions are not supported
     */
    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    /**
     * Acquires in exclusive mode, ignoring interrupts.  Implemented
     * by invoking at least once {@link #tryAcquire},
     * returning on success.  Otherwise the thread is queued, possibly
     * repeatedly blocking and unblocking, invoking {@link
     * #tryAcquire} until success.  This method can be used
     * to implement method {@link Lock#lock}.
     * <p>
     * 在忽略中断的情况下以独占模式获取锁。实现方式是至少调用一次 {@link #tryAcquire}，成功时返回。
     * 否则，线程将排队，可能会反复阻塞和解除阻塞，直到成功为止。此方法可用于实现 {@link Lock#lock} 方法。
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquire} but is otherwise uninterpreted and
     *        can represent anything you like.
     *            @param arg 获取锁的参数。该值传递给 {@link #tryAcquire}，但在其他情况下不进行解释，可以表示任何你想要的内容。
     */
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
                //这一行代码将当前线程加入到同步队列中，并等待获取锁。如果成功获取锁，返回 true，否则返回 false。，
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            //如果在排队等待获取锁的过程中，当前线程被中断过，那么会执行这一行代码，产生一个中断请求
            selfInterrupt();
    }

    /**
     * Acquires in exclusive mode, aborting if interrupted.
     * Implemented by first checking interrupt status, then invoking
     * at least once {@link #tryAcquire}, returning on
     * success.  Otherwise the thread is queued, possibly repeatedly
     * blocking and unblocking, invoking {@link #tryAcquire}
     * until success or the thread is interrupted.  This method can be
     * used to implement method {@link Lock#lockInterruptibly}.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquire} but is otherwise uninterpreted and
     *        can represent anything you like.
     * @throws InterruptedException if the current thread is interrupted
     */
    /**
     * 在可中断模式下以独占模式获取。如果线程被中断，则此操作将中止。
     * 实现方式为：首先检查中断状态，然后至少调用一次{@link #tryAcquire}，
     * 成功则返回。否则，线程将被加入队列，可能经历多次阻塞和解除阻塞的过程，
     * 并重复调用{@link #tryAcquire}直至成功或线程被中断。此方法可用于
     * 实现`Lock#lockInterruptibly`方法。
     *
     * @param arg 获取操作的参数。此值会传递给{@link #tryAcquire}，
     *          除此之外不做特定解释，可以代表任何你想要的值。
     * @throws InterruptedException 如果当前线程被中断
     */
    public final void acquireInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }

    /**
     * Attempts to acquire in exclusive mode, aborting if interrupted,
     * and failing if the given timeout elapses.  Implemented by first
     * checking interrupt status, then invoking at least once {@link
     * #tryAcquire}, returning on success.  Otherwise, the thread is
     * queued, possibly repeatedly blocking and unblocking, invoking
     * {@link #tryAcquire} until success or the thread is interrupted
     * or the timeout elapses.  This method can be used to implement
     * method {@link Lock#tryLock(long, TimeUnit)}.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquire} but is otherwise uninterpreted and
     *        can represent anything you like.
     * @param nanosTimeout the maximum number of nanoseconds to wait
     * @return {@code true} if acquired; {@code false} if timed out
     * @throws InterruptedException if the current thread is interrupted
     */
    public final boolean tryAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquire(arg) ||
            doAcquireNanos(arg, nanosTimeout);
    }

    /**
     * Releases in exclusive mode.  Implemented by unblocking one or
     * more threads if {@link #tryRelease} returns true.
     * This method can be used to implement method {@link Lock#unlock}.
     *
     * @param arg the release argument.  This value is conveyed to
     *        {@link #tryRelease} but is otherwise uninterpreted and
     *        can represent anything you like.
     * @return the value returned from {@link #tryRelease}
     */
    /**
     * 在独占模式下释放。如果{@link #tryRelease}返回true，将唤醒一个或多个等待线程。
     * 此方法可用于实现`Lock#unlock`方法。
     *
     * @param arg 释放参数。此值会传递给{@link #tryRelease}，除此之外不做特定解释，
     *            可以代表任何你想要的值。
     * @return 从{@link #tryRelease}返回的值
     */
    public final boolean release(int arg) {
        // 尝试释放资源，如果成功
        if (tryRelease(arg)) {
            // 获取同步队列的头节点
            Node h = head;
            // 如果头节点不为空且其waitStatus不为0（即存在等待的后继节点）
            if (h != null && h.waitStatus != 0)
                // 唤醒头节点的后继节点对应的线程
                unparkSuccessor(h);
            // 释放成功，返回true
            return true;
        }
        // 释放失败，返回false
        return false;
    }

    /**
     * Acquires in shared mode, ignoring interrupts.  Implemented by
     * first invoking at least once {@link #tryAcquireShared},
     * returning on success.  Otherwise the thread is queued, possibly
     * repeatedly blocking and unblocking, invoking {@link
     * #tryAcquireShared} until success.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquireShared} but is otherwise uninterpreted
     *        and can represent anything you like.
     */
    /**
     * 以共享模式获取，忽略中断。通过首先至少调用一次
     * {@link #tryAcquireShared}实现，成功则返回。否则线程将被排队，
     * 可能会反复阻塞和解除阻塞，调用{@link #tryAcquireShared}直到成功。
     *
     * @param arg 获取参数。这个值会传递给{@link #tryAcquireShared}，
     *        但除此之外没有解释，可以代表任何你喜欢的东西。
     */
    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }

    /**
     * Acquires in shared mode, aborting if interrupted.  Implemented
     * by first checking interrupt status, then invoking at least once
     * {@link #tryAcquireShared}, returning on success.  Otherwise the
     * thread is queued, possibly repeatedly blocking and unblocking,
     * invoking {@link #tryAcquireShared} until success or the thread
     * is interrupted.
     * @param arg the acquire argument.
     * This value is conveyed to {@link #tryAcquireShared} but is
     * otherwise uninterpreted and can represent anything
     * you like.
     * @throws InterruptedException if the current thread is interrupted
     */
    /**
     * 在共享模式下获取，如果被中断则中止操作。
     * 实现方式是先检查中断状态，然后至少调用一次{@link #tryAcquireShared}，
     * 如果成功则返回。否则，线程将被加入队列，可能反复经历阻塞和解除阻塞的过程，
     * 不断调用{@link #tryAcquireShared}直到成功或线程被中断。
     *
     * @param arg 获取操作的参数。
     * 此值会传递给{@link #tryAcquireShared}，除此之外不做特定解释，
     * 可以代表任何你想要的信息。
     * @throws InterruptedException 如果当前线程在等待过程中被中断
     */
    public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
        // 检查当前线程是否已被中断，如果是，则直接抛出InterruptedException
        if (Thread.interrupted())
            throw new InterruptedException();
        // 尝试以共享模式获取资源，如果tryAcquireShared返回小于0表示获取失败
        if (tryAcquireShared(arg) < 0)
            // 如果获取失败，则调用doAcquireSharedInterruptibly进行可中断的等待
            doAcquireSharedInterruptibly(arg);
    }

    /**
     * Attempts to acquire in shared mode, aborting if interrupted, and
     * failing if the given timeout elapses.  Implemented by first
     * checking interrupt status, then invoking at least once {@link
     * #tryAcquireShared}, returning on success.  Otherwise, the
     * thread is queued, possibly repeatedly blocking and unblocking,
     * invoking {@link #tryAcquireShared} until success or the thread
     * is interrupted or the timeout elapses.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquireShared} but is otherwise uninterpreted
     *        and can represent anything you like.
     * @param nanosTimeout the maximum number of nanoseconds to wait
     * @return {@code true} if acquired; {@code false} if timed out
     * @throws InterruptedException if the current thread is interrupted
     */
    /**
     * 尝试以共享模式获取资源，如果被中断或给定的超时期限到达则放弃尝试。实现方式为：
     * 首先检查中断状态，然后至少调用一次{@link #tryAcquireShared}，
     * 如果成功则返回。否则，线程将被加入队列，可能反复经历阻塞和解除阻塞的过程，
     * 不断调用{@link #tryAcquireShared}直到成功、线程被中断或超时。
     *
     * @param arg 获取操作的参数。此值会传递给{@link #tryAcquireShared}，
     *          除此之外不做特定解释，可以表示任何你希望的内容。
     * @param nanosTimeout 等待的最大纳秒数
     * @return 如果获取成功返回{@code true}；如果超时返回{@code false}
     * @throws InterruptedException 如果当前线程在等待过程中被中断
     */
    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        // 首先检查当前线程是否已被中断，如果是，则抛出InterruptedException
        if (Thread.interrupted())
            throw new InterruptedException();
        // 尝试立即以共享模式获取资源，如果tryAcquireShared返回非负值表示成功
        // // 如果立即获取失败，则调用doAcquireSharedNanos尝试在指定超时时间内获取
        return tryAcquireShared(arg) >= 0 ||
            doAcquireSharedNanos(arg, nanosTimeout);
    }

    /**
     * Releases in shared mode.  Implemented by unblocking one or more
     * threads if {@link #tryReleaseShared} returns true.
     *
     * @param arg the release argument.  This value is conveyed to
     *        {@link #tryReleaseShared} but is otherwise uninterpreted
     *        and can represent anything you like.
     * @return the value returned from {@link #tryReleaseShared}
     */
    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }

    // Queue inspection methods

    /**
     * Queries whether any threads are waiting to acquire. Note that
     * because cancellations due to interrupts and timeouts may occur
     * at any time, a {@code true} return does not guarantee that any
     * other thread will ever acquire.
     *
     * <p>In this implementation, this operation returns in
     * constant time.
     * 查询是否有任何线程正在等待获取同步器。请注意，由于中断和超时导致的取消可能随时发生，返回
     * {@code true} 并不保证任何其他线程将会获取同步器。
     * @return {@code true} if there may be other threads waiting to acquire  {@code true} 如果可能有其他线程正在等待获取
     */
    public final boolean hasQueuedThreads() {
        return head != tail;
    }

    /**
     * Queries whether any threads have ever contended to acquire this
     * synchronizer; that is if an acquire method has ever blocked.
     *
     * <p>In this implementation, this operation returns in
     * constant time.
     * 查询是否有任何线程曾经争用过该同步器；也就是说，是否有获取方法曾经被阻塞。
     *
     * <p>在此实现中，此操作在常数时间内返回。

     * @return {@code true} if there has ever been contention
     */
    public final boolean hasContended() {
        return head != null;
    }

    /**
     * Returns the first (longest-waiting) thread in the queue, or
     * {@code null} if no threads are currently queued.
     *
     * <p>In this implementation, this operation normally returns in
     * constant time, but may iterate upon contention if other threads are
     * concurrently modifying the queue.
     *
     * @return the first (longest-waiting) thread in the queue, or
     *         {@code null} if no threads are currently queued
     */
    /**
     * 返回队列中最先（等待时间最长）的线程，如果没有线程在队列中等待，则返回{@code null}。
     *
     * <p>在此实现中，此操作通常在常数时间内完成，但在其他线程并发修改队列时可能会进行迭代。
     *
     * @return 队列中最先（等待时间最长）的线程，如果没有线程在队列中等待，则返回{@code null}
     */
    public final Thread getFirstQueuedThread() {
        // handle only fast path, else relay
        // 只处理快速路径情况，其他情况转发给完整方法处理
        return (head == tail) ? null : fullGetFirstQueuedThread();
    }

    /**
     * Version of getFirstQueuedThread called when fastpath fails
     */
    /**
     * 当快速路径检查失败时调用的获取队列首线程版本。
     */
    private Thread fullGetFirstQueuedThread() {
        /*
         * The first node is normally head.next. Try to get its
         * thread field, ensuring consistent reads: If thread
         * field is nulled out or s.prev is no longer head, then
         * some other thread(s) concurrently performed setHead in
         * between some of our reads. We try this twice before
         * resorting to traversal.
         */
        /*
         * 首个节点通常为head的下一个节点。尝试获取它的thread字段，确保一致读取：
         * 如果thread字段被清空或s.prev不再是head，则在我们的某些读取之间其他线程并发执行了setHead操作。
         * 我们尝试这一步两次，然后再转向遍历。
         */
        Node h, s;
        Thread st;
        if (((h = head) != null && (s = h.next) != null &&
             s.prev == head && (st = s.thread) != null) ||
            ((h = head) != null && (s = h.next) != null &&
             s.prev == head && (st = s.thread) != null))
            return st;

        /*
         * Head's next field might not have been set yet, or may have
         * been unset after setHead. So we must check to see if tail
         * is actually first node. If not, we continue on, safely
         * traversing from tail back to head to find first,
         * guaranteeing termination.
         */
        /*
         * head的next字段可能还没被设置，或者在setHead之后被重置。所以我们必须检查tail是否实际上是第一个节点。
         * 如果不是，我们将继续安全地从tail向head方向遍历来寻找第一个线程，确保能找到并终止遍历。
         */
        Node t = tail; // 从尾部开始遍历
        Thread firstThread = null;
        while (t != null && t != head) { // 遍历直到回到head或遇到null
            Thread tt = t.thread; // 获取当前节点的线程引用
            if (tt != null)
                firstThread = tt; // 找到有效的线程引用，记录下来
            t = t.prev; // 移动到前一个节点
        }
        return firstThread; // 返回找到的第一个线程
    }

    /**
     * Returns true if the given thread is currently queued.
     *
     * <p>This implementation traverses the queue to determine
     * presence of the given thread.
     *如果给定的线程当前在队列中，则返回true。
     *
     * <p>此实现遍历队列以确定给定线程是否存在。
     * @param thread the thread 线程
     * @return {@code true} if the given thread is on the queue  {@code true} 如果给定线程在队列中
     * @throws NullPointerException if the thread is null NullPointerException 如果线程为空
     */
    public final boolean isQueued(Thread thread) {
        if (thread == null)
            throw new NullPointerException(); // 如果传入的线程为空，则抛出空指针异常。
        for (Node p = tail; p != null; p = p.prev) // 从队列的尾部开始遍历，逐个节点向前检查。
            if (p.thread == thread) // 如果当前节点的线程与传入的线程相同，返回true。
                return true;
        return false; // 如果遍历完队列没有找到对应的线程，返回false。
    }

    /**
     * Returns {@code true} if the apparent first queued thread, if one
     * exists, is waiting in exclusive mode.  If this method returns
     * {@code true}, and the current thread is attempting to acquire in
     * shared mode (that is, this method is invoked from {@link
     * #tryAcquireShared}) then it is guaranteed that the current thread
     * is not the first queued thread.  Used only as a heuristic in
     * ReentrantReadWriteLock.
     */
    /**
     * 如果存在明显首个排队的线程，并且该线程正在独占模式下等待，则返回{@code true}。
     * 如果此方法返回{@code true}，且当前线程正尝试以共享模式获取（即，该方法从{@link #tryAcquireShared}调用），
     * 则可以确保当前线程不是队列中的首个等待线程。
     * 此方法仅用作启发式判断，在`ReentrantReadWriteLock`等场景中使用。
     */
    /**
     * 这个方法主要用于并发控制组件内部逻辑的优化，
     * 特别是当需要快速判断当前线程是否能够安全地进行某些操作时
     * （例如，在读写锁中，如果确定当前线程不是首个等待的独占锁请求者，那么尝试获取读锁的线程可以不必进一步检查）。
     * 它提供了一种高效的方式来帮助决定是否需要进行更深入的同步控制逻辑，从而提高整体的并发性能。
     * @return
     */
    final boolean apparentlyFirstQueuedIsExclusive() {
        Node h, s; // 定义两个临时变量用于分别存储头节点和第二个节点
        // 检查以下条件是否全部满足：
        // 1. 队列的头节点不为null，表示队列非空
        // 2. 头节点的下一个节点也不为null，意味着至少有两个节点在队列中
        // 3. 第二个节点（即头节点的下一个节点）是非共享的，表示它在独占模式下等待
        // 4. 第二个节点关联了一个线程，确保该节点有效
        return (h = head) != null &&
            (s = h.next)  != null &&
            !s.isShared()         &&
            s.thread != null;
    }

    /**
     * Queries whether any threads have been waiting to acquire longer
     * than the current thread.
     *
     * <p>An invocation of this method is equivalent to (but may be
     * more efficient than):
     *  <pre> {@code
     * getFirstQueuedThread() != Thread.currentThread() &&
     * hasQueuedThreads()}</pre>
     *
     * <p>Note that because cancellations due to interrupts and
     * timeouts may occur at any time, a {@code true} return does not
     * guarantee that some other thread will acquire before the current
     * thread.  Likewise, it is possible for another thread to win a
     * race to enqueue after this method has returned {@code false},
     * due to the queue being empty.
     *
     * <p>This method is designed to be used by a fair synchronizer to
     * avoid <a href="AbstractQueuedSynchronizer#barging">barging</a>.
     * Such a synchronizer's {@link #tryAcquire} method should return
     * {@code false}, and its {@link #tryAcquireShared} method should
     * return a negative value, if this method returns {@code true}
     * (unless this is a reentrant acquire).  For example, the {@code
     * tryAcquire} method for a fair, reentrant, exclusive mode
     * synchronizer might look like this:
     *
     *  <pre> {@code
     * protected boolean tryAcquire(int arg) {
     *   if (isHeldExclusively()) {
     *     // A reentrant acquire; increment hold count
     *     return true;
     *   } else if (hasQueuedPredecessors()) {
     *     return false;
     *   } else {
     *     // try to acquire normally
     *   }
     * }}</pre>
     *
     * @return {@code true} if there is a queued thread preceding the
     *         current thread, and {@code false} if the current thread
     *         is at the head of the queue or the queue is empty
     * @since 1.7
     */
    public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        Node t = tail; // Read fields in reverse initialization order
        Node h = head;
        Node s;
        return h != t &&
            ((s = h.next) == null || s.thread != Thread.currentThread());
    }


    // Instrumentation and monitoring methods

    /**
     * Returns an estimate of the number of threads waiting to
     * acquire.  The value is only an estimate because the number of
     * threads may change dynamically while this method traverses
     * internal data structures.  This method is designed for use in
     * monitoring system state, not for synchronization
     * control.
     *
     * @return the estimated number of threads waiting to acquire
     */
    public final int getQueueLength() {
        int n = 0;
        for (Node p = tail; p != null; p = p.prev) {
            if (p.thread != null)
                ++n;
        }
        return n;
    }

    /**
     * Returns a collection containing threads that may be waiting to
     * acquire.  Because the actual set of threads may change
     * dynamically while constructing this result, the returned
     * collection is only a best-effort estimate.  The elements of the
     * returned collection are in no particular order.  This method is
     * designed to facilitate construction of subclasses that provide
     * more extensive monitoring facilities.
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            Thread t = p.thread;
            if (t != null)
                list.add(t);
        }
        return list;
    }

    /**
     * Returns a collection containing threads that may be waiting to
     * acquire in exclusive mode. This has the same properties
     * as {@link #getQueuedThreads} except that it only returns
     * those threads waiting due to an exclusive acquire.
     * 返回一个包含可能正在以独占模式等待获取同步器的线程的集合。
     * 这具有与 {@link #getQueuedThreads} 相同的属性，只不过它只返回那些
     * 因独占获取而等待的线程。
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getExclusiveQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>(); // 创建一个新的ArrayList来保存线程
        for (Node p = tail; p != null; p = p.prev) { // 从队列的尾部开始向前遍历
            if (!p.isShared()) { // 只处理独占模式的节点
                Thread t = p.thread; // 获取节点中的线程
                if (t != null)
                    list.add(t); // 如果线程不为空，将其添加到列表中
            }
        }
        return list;
    }

    /**
     * 同理如上。
     * Returns a collection containing threads that may be waiting to
     * acquire in shared mode. This has the same properties
     * as {@link #getQueuedThreads} except that it only returns
     * those threads waiting due to a shared acquire.
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getSharedQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            if (p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    /**
     * Returns a string identifying this synchronizer, as well as its state.
     * The state, in brackets, includes the String {@code "State ="}
     * followed by the current value of {@link #getState}, and either
     * {@code "nonempty"} or {@code "empty"} depending on whether the
     * queue is empty.
     * 返回一个标识此同步器及其状态的字符串。
     * 状态部分包含字符串 {@code "State ="} 后跟当前的 {@link #getState} 值，
     * 并且根据队列是否为空显示 {@code "nonempty"} 或 {@code "empty"}。
     *
     * @return a string identifying this synchronizer, as well as its state
     */
    public String toString() {
        int s = getState();
        String q  = hasQueuedThreads() ? "non" : "";
        return super.toString() +
            "[State = " + s + ", " + q + "empty queue]";
    }


    // Internal support methods for Conditions

    /**
     * Returns true if a node, always one that was initially placed on
     * a condition queue, is now waiting to reacquire on sync queue.
     * @param node the node
     * @return true if is reacquiring
     * 如果一个节点（总是最初放置在条件队列上的节点）现在正在同步队列上等待重新获取锁，则返回 true。
     * @param node 节点
     * @return 如果正在重新获取锁，则返回 true
     */
    final boolean isOnSyncQueue(Node node) {
        if (node.waitStatus == Node.CONDITION || node.prev == null)
            return false;
        if (node.next != null) // If has successor, it must be on queue
            return true;
        /*
         * node.prev can be non-null, but not yet on queue because
         * the CAS to place it on queue can fail. So we have to
         * traverse from tail to make sure it actually made it.  It
         * will always be near the tail in calls to this method, and
         * unless the CAS failed (which is unlikely), it will be
         * there, so we hardly ever traverse much.
         */
        /* 这里的意思是 node.prev不为null的时候，依然可能因为cas失败而无法放到队列上。
         * node.prev 可以是非空的，但尚未在队列上，因为将其放置在队列上的 CAS 操作可能会失败。
         * 因此我们必须从尾部开始遍历以确保它确实在队列上。在对该方法的调用中，它总是接近尾部，
         * 除非 CAS 失败（这不太可能），它将会在队列上，所以我们几乎不需要遍历很多。
         */
        return findNodeFromTail(node);
    }

    /**
     * Returns true if node is on sync queue by searching backwards from tail.
     * Called only when needed by isOnSyncQueue.
     * @return true if present
     * 如果节点在同步队列中，则通过从尾部向后搜索返回 true。
     * 仅在 isOnSyncQueue 方法需要时调用。
     * @return 如果存在则返回 true
     */
    private boolean findNodeFromTail(Node node) {
        Node t = tail;
        for (;;) {
            if (t == node)
                return true;
            if (t == null)
                return false;
            t = t.prev;
        }
    }

    /**
     * Transfers a node from a condition queue onto sync queue.
     * Returns true if successful.
     * @param node the node
     * @return true if successfully transferred (else the node was
     * cancelled before signal)
     */
    final boolean transferForSignal(Node node) {
        /*
         * If cannot change waitStatus, the node has been cancelled.
         */
        if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
            return false;

        /*
         * Splice onto queue and try to set waitStatus of predecessor to
         * indicate that thread is (probably) waiting. If cancelled or
         * attempt to set waitStatus fails, wake up to resync (in which
         * case the waitStatus can be transiently and harmlessly wrong).
         */
        Node p = enq(node);
        int ws = p.waitStatus;
        if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL))
            LockSupport.unpark(node.thread);
        return true;
    }

    /**
     * Transfers node, if necessary, to sync queue after a cancelled wait.
     * Returns true if thread was cancelled before being signalled.
     *
     * @param node the node
     * @return true if cancelled before the node was signalled
     */
    final boolean transferAfterCancelledWait(Node node) {
        if (compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
            enq(node);
            return true;
        }
        /*
         * If we lost out to a signal(), then we can't proceed
         * until it finishes its enq().  Cancelling during an
         * incomplete transfer is both rare and transient, so just
         * spin.
         */
        while (!isOnSyncQueue(node))
            Thread.yield();
        return false;
    }

    /**
     * Invokes release with current state value; returns saved state.
     * Cancels node and throws exception on failure.
     * @param node the condition node for this wait
     * @return previous sync state
     * 调用release方法并使用当前的同步状态值；返回保存的状态值。
     * 如果失败，取消节点并抛出异常。
     * @param node 这次等待的条件节点
     * @return 之前的同步状态
     */
    final int fullyRelease(Node node) {
        boolean failed = true; // 默认设置为失败
        try {
            int savedState = getState(); // 获取当前的同步状态
            if (release(savedState)) {  // 尝试释放锁
                failed = false; // 如果成功，设置失败为false
                return savedState;  // 返回之前保存的状态值
            } else {
                throw new IllegalMonitorStateException();  // 如果释放失败，抛出异常
            }
        } finally {
            if (failed) // 如果最终失败
                node.waitStatus = Node.CANCELLED;  // 设置节点状态为取消
        }
    }

    // Instrumentation methods for conditions

    /**
     * Queries whether the given ConditionObject
     * uses this synchronizer as its lock.
     *
     * @param condition the condition
     * @return {@code true} if owned
     * @throws NullPointerException if the condition is null
     * 查询给定的ConditionObject是否使用此同步器作为其锁。
     *
     * @param condition 条件对象
     * @return {@code true} 如果是由此同步器拥有
     * @throws NullPointerException 如果条件对象为null
     */
    public final boolean owns(ConditionObject condition) {
        return condition.isOwnedBy(this);  // 调用ConditionObject的isOwnedBy方法来检查
    }

    /**
     * Queries whether any threads are waiting on the given condition
     * associated with this synchronizer. Note that because timeouts
     * and interrupts may occur at any time, a {@code true} return
     * does not guarantee that a future {@code signal} will awaken
     * any threads.  This method is designed primarily for use in
     * monitoring of the system state.
     *
     * @param condition the condition
     * @return {@code true} if there are any waiting threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *         is not held
     * @throws IllegalArgumentException if the given condition is
     *         not associated with this synchronizer
     * @throws NullPointerException if the condition is null
     */
    public final boolean hasWaiters(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.hasWaiters();
    }

    /**
     * Returns an estimate of the number of threads waiting on the
     * given condition associated with this synchronizer. Note that
     * because timeouts and interrupts may occur at any time, the
     * estimate serves only as an upper bound on the actual number of
     * waiters.  This method is designed for use in monitoring of the
     * system state, not for synchronization control.
     *
     * @param condition the condition
     * @return the estimated number of waiting threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *         is not held
     * @throws IllegalArgumentException if the given condition is
     *         not associated with this synchronizer
     * @throws NullPointerException if the condition is null
     */
    public final int getWaitQueueLength(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitQueueLength();
    }

    /**
     * Returns a collection containing those threads that may be
     * waiting on the given condition associated with this
     * synchronizer.  Because the actual set of threads may change
     * dynamically while constructing this result, the returned
     * collection is only a best-effort estimate. The elements of the
     * returned collection are in no particular order.
     *
     * @param condition the condition
     * @return the collection of threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *         is not held
     * @throws IllegalArgumentException if the given condition is
     *         not associated with this synchronizer
     * @throws NullPointerException if the condition is null
     */
    public final Collection<Thread> getWaitingThreads(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitingThreads();
    }

    /**
     * Condition implementation for a {@link
     * AbstractQueuedSynchronizer} serving as the basis of a {@link
     * Lock} implementation.
     *
     * <p>Method documentation for this class describes mechanics,
     * not behavioral specifications from the point of view of Lock
     * and Condition users. Exported versions of this class will in
     * general need to be accompanied by documentation describing
     * condition semantics that rely on those of the associated
     * {@code AbstractQueuedSynchronizer}.
     *
     * <p>This class is Serializable, but all fields are transient,
     * so deserialized conditions have no waiters.
     *
     * 为{@link AbstractQueuedSynchronizer}提供的{@link Lock}实现的Condition实现。
     *
     * <p>此类的方法文档描述了机制，
     * 而不是从Lock和Condition用户的角度出发的行为规范。
     * 导出的此类版本通常需要伴随着描述
     * 依赖于关联的{@code AbstractQueuedSynchronizer}的条件语义的文档。
     *
     * <p>此类是可序列化的，但所有字段都是瞬态的，
     * 因此反序列化的条件没有等待者。
     */
    public class ConditionObject implements Condition, java.io.Serializable {
        private static final long serialVersionUID = 1173984872572414699L;
        /** First node of condition queue. */
        private transient Node firstWaiter;
        /** Last node of condition queue. */
        private transient Node lastWaiter;

        /**
         * Creates a new {@code ConditionObject} instance.
         */
        public ConditionObject() { }

        // Internal methods

        /**
         * Adds a new waiter to wait queue.
         * @return its new wait node
         */
        private Node addConditionWaiter() {
            Node t = lastWaiter;
            // If lastWaiter is cancelled, clean out.
            if (t != null && t.waitStatus != Node.CONDITION) {
                unlinkCancelledWaiters();
                t = lastWaiter;
            }
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            if (t == null)
                firstWaiter = node;
            else
                t.nextWaiter = node;
            lastWaiter = node;
            return node;
        }

        /**
         * Removes and transfers nodes until hit non-cancelled one or
         * null. Split out from signal in part to encourage compilers
         * to inline the case of no waiters.
         * @param first (non-null) the first node on condition queue
         */
        private void doSignal(Node first) {
            do {
                if ( (firstWaiter = first.nextWaiter) == null)
                    lastWaiter = null;
                first.nextWaiter = null;
            } while (!transferForSignal(first) &&
                     (first = firstWaiter) != null);
        }

        /**
         * Removes and transfers all nodes.
         * @param first (non-null) the first node on condition queue
         */
        private void doSignalAll(Node first) {
            lastWaiter = firstWaiter = null;
            do {
                Node next = first.nextWaiter;
                first.nextWaiter = null;
                transferForSignal(first);
                first = next;
            } while (first != null);
        }

        /**
         * Unlinks cancelled waiter nodes from condition queue.
         * Called only while holding lock. This is called when
         * cancellation occurred during condition wait, and upon
         * insertion of a new waiter when lastWaiter is seen to have
         * been cancelled. This method is needed to avoid garbage
         * retention in the absence of signals. So even though it may
         * require a full traversal, it comes into play only when
         * timeouts or cancellations occur in the absence of
         * signals. It traverses all nodes rather than stopping at a
         * particular target to unlink all pointers to garbage nodes
         * without requiring many re-traversals during cancellation
         * storms.
         */
        private void unlinkCancelledWaiters() {
            Node t = firstWaiter;
            Node trail = null;
            while (t != null) {
                Node next = t.nextWaiter;
                if (t.waitStatus != Node.CONDITION) {
                    t.nextWaiter = null;
                    if (trail == null)
                        firstWaiter = next;
                    else
                        trail.nextWaiter = next;
                    if (next == null)
                        lastWaiter = trail;
                }
                else
                    trail = t;
                t = next;
            }
        }

        // public methods

        /**
         * Moves the longest-waiting thread, if one exists, from the
         * wait queue for this condition to the wait queue for the
         * owning lock.
         *
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        public final void signal() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignal(first);
        }

        /**
         * Moves all threads from the wait queue for this condition to
         * the wait queue for the owning lock.
         *
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        public final void signalAll() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignalAll(first);
        }

        /**
         * Implements uninterruptible condition wait.
         * <ol>
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * </ol>
         */
        public final void awaitUninterruptibly() {
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean interrupted = false;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if (Thread.interrupted())
                    interrupted = true;
            }
            if (acquireQueued(node, savedState) || interrupted)
                selfInterrupt();
        }

        /*
         * For interruptible waits, we need to track whether to throw
         * InterruptedException, if interrupted while blocked on
         * condition, versus reinterrupt current thread, if
         * interrupted while blocked waiting to re-acquire.
         */

        /** Mode meaning to reinterrupt on exit from wait */
        private static final int REINTERRUPT =  1;
        /** Mode meaning to throw InterruptedException on exit from wait */
        private static final int THROW_IE    = -1;

        /**
         * Checks for interrupt, returning THROW_IE if interrupted
         * before signalled, REINTERRUPT if after signalled, or
         * 0 if not interrupted.
         */
        private int checkInterruptWhileWaiting(Node node) {
            return Thread.interrupted() ?
                (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) :
                0;
        }

        /**
         * Throws InterruptedException, reinterrupts current thread, or
         * does nothing, depending on mode.
         */
        private void reportInterruptAfterWait(int interruptMode)
            throws InterruptedException {
            if (interruptMode == THROW_IE)
                throw new InterruptedException();
            else if (interruptMode == REINTERRUPT)
                selfInterrupt();
        }

        /**
         * Implements interruptible condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled or interrupted.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * </ol>
         */
        public final void await() throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null) // clean up if cancelled
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
        }

        /**
         * Implements timed condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled, interrupted, or timed out.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * </ol>
         */
        public final long awaitNanos(long nanosTimeout)
                throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return deadline - System.nanoTime();
        }

        /**
         * Implements absolute timed condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled, interrupted, or timed out.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * <li> If timed out while blocked in step 4, return false, else true.
         * </ol>
         */
        public final boolean awaitUntil(Date deadline)
                throws InterruptedException {
            long abstime = deadline.getTime();
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (System.currentTimeMillis() > abstime) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkUntil(this, abstime);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        /**
         * Implements timed condition wait.
         * <ol>
         * <li> If current thread is interrupted, throw InterruptedException.
         * <li> Save lock state returned by {@link #getState}.
         * <li> Invoke {@link #release} with saved state as argument,
         *      throwing IllegalMonitorStateException if it fails.
         * <li> Block until signalled, interrupted, or timed out.
         * <li> Reacquire by invoking specialized version of
         *      {@link #acquire} with saved state as argument.
         * <li> If interrupted while blocked in step 4, throw InterruptedException.
         * <li> If timed out while blocked in step 4, return false, else true.
         * </ol>
         */
        public final boolean await(long time, TimeUnit unit)
                throws InterruptedException {
            long nanosTimeout = unit.toNanos(time);
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        //  support for instrumentation

        /**
         * Returns true if this condition was created by the given
         * synchronization object.
         *
         * @return {@code true} if owned
         */
        final boolean isOwnedBy(AbstractQueuedSynchronizer sync) {
            return sync == AbstractQueuedSynchronizer.this;
        }

        /**
         * Queries whether any threads are waiting on this condition.
         * Implements {@link AbstractQueuedSynchronizer#hasWaiters(ConditionObject)}.
         *
         * @return {@code true} if there are any waiting threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        protected final boolean hasWaiters() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    return true;
            }
            return false;
        }

        /**
         * Returns an estimate of the number of threads waiting on
         * this condition.
         * Implements {@link AbstractQueuedSynchronizer#getWaitQueueLength(ConditionObject)}.
         *
         * @return the estimated number of waiting threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        protected final int getWaitQueueLength() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            int n = 0;
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    ++n;
            }
            return n;
        }

        /**
         * Returns a collection containing those threads that may be
         * waiting on this Condition.
         * Implements {@link AbstractQueuedSynchronizer#getWaitingThreads(ConditionObject)}.
         *
         * @return the collection of threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        protected final Collection<Thread> getWaitingThreads() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            ArrayList<Thread> list = new ArrayList<Thread>();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION) {
                    Thread t = w.thread;
                    if (t != null)
                        list.add(t);
                }
            }
            return list;
        }
    }

    /**
     * Setup to support compareAndSet. We need to natively implement
     * this here: For the sake of permitting future enhancements, we
     * cannot explicitly subclass AtomicInteger, which would be
     * efficient and useful otherwise. So, as the lesser of evils, we
     * natively implement using hotspot intrinsics API. And while we
     * are at it, we do the same for other CASable fields (which could
     * otherwise be done with atomic field updaters).
     */
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                (Node.class.getDeclaredField("next"));

        } catch (Exception ex) { throw new Error(ex); }
    }

    /**
     * CAS head field. Used only by enq.
     */
    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    /**
     * CAS tail field. Used only by enq.
     */
    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    /**
     * CAS waitStatus field of a node.
     */
    private static final boolean compareAndSetWaitStatus(Node node,
                                                         int expect,
                                                         int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset,
                                        expect, update);
    }

    /**
     * CAS next field of a node.
     */
    private static final boolean compareAndSetNext(Node node,
                                                   Node expect,
                                                   Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
}
