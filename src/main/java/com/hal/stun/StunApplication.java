package com.hal.stun;

import com.hal.stun.message.StunMessage;
import com.hal.stun.message.StunResponseMessage;
import com.hal.stun.message.StunHeader;
import com.hal.stun.message.MessageClass;
import com.hal.stun.message.StunParseException;

class StunApplication {
  
  public StunApplication() {
  }
  
  // handler also needs some info about the connection like
  // source IP, request received time, etc.
  public byte[] handle(byte[] rawRequest) throws UnsupportedStunClassException, StunParseException {
    StunMessage request = new StunMessage(rawRequest);
    StunMessage response = buildResponse(request);
    return response.toByteArray();
  }
  
  public static StunMessage buildResponse(StunMessage request) throws UnsupportedStunClassException {
    MessageClass requestMessageClass = request.getHeader().getMessageClass();
    StunHeader header = request.getHeader();
    StunMessage response;
    switch(requestMessageClass) {
      case REQUEST:
      response = handleRequest(request);
      break;
      case INDICATION:
      handleIndication(request);
      response = null;
      break;
      case SUCCESS:
      case ERROR:
      default:
      throw new UnsupportedStunClassException(requestMessageClass);
    }
    
    return response;
  }
  
  public static StunMessage handleRequest(StunMessage request) {
    StunMessage response = new StunResponseMessage(request);
    return response;
  }
  
  //TODO: implement indication
  public static void handleIndication(StunMessage request) {
  }
  
  public static class UnsupportedStunClassException extends Exception {
    
    public UnsupportedStunClassException(MessageClass messageClass) {
      super("stun message type " + messageClass.name() + " is not supported by the STUN server");
    }
    
  }
  
}