package org.omg.PortableInterceptor;


/**
* org/omg/PortableInterceptor/RequestInfo.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/openjdk/jdk8u/corba/src/share/classes/org/omg/PortableInterceptor/Interceptors.idl
* Tuesday, January 30, 2024 5:01:39 AM EST
*/


/**
   * Request Information, accessible to Interceptors.
   * <p>
   * Each interception point is given an object through which the 
   * Interceptor can access request information. Client-side and server-side 
   * interception points are concerned with different information, so there 
   * are two information objects: <code>ClientRequestInfo</code> is passed 
   * to the client-side interception points and <code>ServerRequestInfo</code>
   * is passed to the server-side interception points. But there is 
   * information that is common to both, so they both inherit from a common 
   * interface: <code>RequestInfo</code>.
   *
   * @see ClientRequestInfo
   * @see ServerRequestInfo
   */
public interface RequestInfo extends RequestInfoOperations, org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity 
{
} // interface RequestInfo
