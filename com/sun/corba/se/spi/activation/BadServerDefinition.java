package com.sun.corba.se.spi.activation;


/**
* com/sun/corba/se/spi/activation/BadServerDefinition.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/openjdk/jdk8u/corba/src/share/classes/com/sun/corba/se/spi/activation/activation.idl
* Tuesday, January 30, 2024 5:01:34 AM EST
*/

public final class BadServerDefinition extends org.omg.CORBA.UserException
{
  public String reason = null;

  public BadServerDefinition ()
  {
    super(BadServerDefinitionHelper.id());
  } // ctor

  public BadServerDefinition (String _reason)
  {
    super(BadServerDefinitionHelper.id());
    reason = _reason;
  } // ctor


  public BadServerDefinition (String $reason, String _reason)
  {
    super(BadServerDefinitionHelper.id() + "  " + $reason);
    reason = _reason;
  } // ctor

} // class BadServerDefinition
