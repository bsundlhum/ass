
package com.codeassessment.ledgerassement.app.excutor.rest;

import com.codeassessment.ledgerassement.domain.PermittableGroupIds;
import com.codeassessment.ledgerassement.domain.model.AcceptedTokenType;
import com.codeassessment.ledgerassement.domain.model.Permittable;
import com.codeassessment.ledgerassement.domain.model.TrialBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codeassessment.ledgerassement.app.excutor.service.services.TrialBalanceService;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/trialbalance")
public class TrialBalanceController {

  private final TrialBalanceService trialBalanceService;

  @Autowired
  public TrialBalanceController(final TrialBalanceService trialBalanceService) {
    super();
    this.trialBalanceService = trialBalanceService;
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.ASSESS_LEDGER)
  @RequestMapping(
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.ALL_VALUE}
  )
  @ResponseBody
  public ResponseEntity<TrialBalance> getTrialBalance(
      @RequestParam(value = "includeEmptyEntries", required = false) final boolean includeEmptyEntries) {
    return ResponseEntity.ok(this.trialBalanceService.getTrialBalance(includeEmptyEntries));
  }
}
