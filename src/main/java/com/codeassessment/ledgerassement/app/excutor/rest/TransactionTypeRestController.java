package com.codeassessment.ledgerassement.app.excutor.rest;


import com.codeassessment.ledgerassement.app.excutor.service.command.ChangeTransactionTypeCommand;
import com.codeassessment.ledgerassement.app.excutor.service.command.CreateTransactionTypeCommand;
import com.codeassessment.ledgerassement.app.excutor.service.services.CommandGateway;
import com.codeassessment.ledgerassement.app.excutor.service.services.TransactionTypeService;
import com.codeassessment.ledgerassement.app.excutor.util.ServiceException;
import com.codeassessment.ledgerassement.domain.PermittableGroupIds;
import com.codeassessment.ledgerassement.domain.model.AcceptedTokenType;
import com.codeassessment.ledgerassement.domain.model.Permittable;
import com.codeassessment.ledgerassement.domain.model.TransactionType;
import com.codeassessment.ledgerassement.domain.model.TransactionTypeAPI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/transactiontypes")
public class TransactionTypeRestController {

  private final CommandGateway commandGateway;
  private final TransactionTypeService transactionTypeService;

  @Autowired
  public TransactionTypeRestController(final CommandGateway commandGateway,
                                       final TransactionTypeService transactionTypeService) {
    super();
    this.commandGateway = commandGateway;
    this.transactionTypeService = transactionTypeService;
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.ASSESS_TX_TYPES)
  @RequestMapping(
      value = "",
      method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE}
  )
  @ResponseBody
  ResponseEntity<Void> createTransactionType(@RequestBody @Valid final TransactionType transactionType) {
    if (this.transactionTypeService.findByIdentifier(transactionType.getCode()).isPresent()) {
      throw ServiceException.conflict("Transaction type '{0}' already exists.", transactionType.getCode());
    }

    this.commandGateway.process(new CreateTransactionTypeCommand(transactionType));
    return ResponseEntity.accepted().build();
  }


  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.ASSESS_TX_TYPES)
  @RequestMapping(
      value = "",
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.ALL_VALUE}
  )
  @ResponseBody
  ResponseEntity<TransactionTypeAPI> fetchTransactionTypes(@RequestParam(value = "term", required = false) final String term,
                                                           @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
                                                           @RequestParam(value = "size", required = false) final Integer size,
                                                           @RequestParam(value = "sortColumn", required = false) final String sortColumn,
                                                           @RequestParam(value = "sortDirection", required = false) final String sortDirection) {
    final String column2sort = "code".equalsIgnoreCase(sortColumn) ? "identifier" : sortColumn;
    return ResponseEntity.ok(
        this.transactionTypeService.fetchTransactionTypes(term,
            PageableBuilder.create(pageIndex, size, column2sort, sortDirection)));
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.ASSESS_TX_TYPES)
  @RequestMapping(
      value = "/{code}",
      method = RequestMethod.PUT,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE}
  )
  @ResponseBody
  ResponseEntity<Void> changeTransactionType(@PathVariable("code") final String code,
                                             @RequestBody @Valid final TransactionType transactionType) {
    if (!code.equals(transactionType.getCode())) {
      throw ServiceException.badRequest("Given transaction type {0} must match request path.", code);
    }

    if (!this.transactionTypeService.findByIdentifier(code).isPresent()) {
      throw ServiceException.notFound("Transaction type '{0}' not found.", code);
    }

    this.commandGateway.process(new ChangeTransactionTypeCommand(transactionType));

    return ResponseEntity.accepted().build();
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.ASSESS_TX_TYPES)
  @RequestMapping(
      value = "/{code}",
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.ALL_VALUE}
  )
  @ResponseBody
  ResponseEntity<TransactionType> findTransactionType(@PathVariable("code") final String code) {
    return ResponseEntity.ok(
        this.transactionTypeService.findByIdentifier(code)
            .orElseThrow(() -> ServiceException.notFound("Transaction type '{0}' not found.", code))
    );
  }
}
