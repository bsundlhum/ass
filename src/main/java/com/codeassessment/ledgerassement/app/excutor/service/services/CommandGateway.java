package com.codeassessment.ledgerassement.app.excutor.service.services;

import com.codeassessment.ledgerassement.app.excutor.service.repository.CommandBus;
import com.codeassessment.ledgerassement.domain.model.CommandProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandGateway {

    private final CommandBus commandBus;

    @Autowired
    public CommandGateway(final CommandBus commandBus) {
        super();
        this.commandBus = commandBus;
    }

    public <C> void process(final C command) {
        this.commandBus.dispatch(command);
    }

    public <C, T> CommandCallback<T> process(final C command, Class<T> clazz) throws CommandProcessingException, CommandProcessingException {
        return new CommandCallback<>(this.commandBus.dispatch(command, clazz));
    }
}
