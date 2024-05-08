package com.codeassessment.ledgerassement.app.excutor.service.mapper;


import com.codeassessment.ledgerassement.app.excutor.service.repository.CommandEntity;
import com.codeassessment.ledgerassement.domain.model.AccountCommand;

public class AccountCommandMapper {

  public AccountCommandMapper() {
    super();
  }

  public static AccountCommand map(final CommandEntity commandEntity){
    final AccountCommand command = new AccountCommand();
    command.setAction(commandEntity.getType());
    command.setComment(commandEntity.getComment());
    command.setCreatedBy(commandEntity.getCreatedBy());
    command.setCreatedOn(commandEntity.getCreatedOn().toString());
    return command;
  }
}
