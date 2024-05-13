
enum MessageType{
  ACK,
  ERROR,
  RESPONSE,
}


export class Message{
  constructor(type : MessageType){}
}

export class ErrorMessage extends Message{
  constructor(reason: string){
    super(MessageType.ERROR);
  }
}

export class OkMessage extends Message{
  constructor(){
    super(MessageType.ACK);
  }
}

export class ResponseMessage<T> extends Message{
  constructor(payload: T){
    super(MessageType.RESPONSE);
  }
}
