/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bankslips;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Josh Long
 */
// tag::code[]
@RestController
@Api(value="bankslips", description="Operations pertaining to bankslips")
@RequestMapping("/bankslips")
class BankSlipRestController {

	private final BankSlipRepository bankSlipRepository;

	@Autowired
	BankSlipRestController(BankSlipRepository bankSlipRepository) {
		this.bankSlipRepository = bankSlipRepository;
	}

	@ApiOperation(value = "Read a BankSlip and calculate tax if is late", response = BankSlip.class)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 404, message = "Bankslip not found with the specified id")
	})
	@GetMapping("/{id}")
	BankSlip readBankSlip(@PathVariable String id) {
		return this.bankSlipRepository.findById(id)
				.orElseThrow(BankSlipNotFoundException::new);
	}

	@ApiOperation(value = "Receave a Bankslip and inserts in a database", response = BankSlip.class)
	@ApiResponses({
			@ApiResponse(code = 201, message = "Bankslip created"),
			@ApiResponse(code = 400, message = "Bankslip not provided in the request body"),
			@ApiResponse(code = 422, message = "Invalid bankslip provided")
	})
	@PostMapping
	ResponseEntity<BankSlip> add(@RequestBody BankSlip bankSlip) {
		return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.bankSlipRepository.save(bankSlip));
	}

	@ApiOperation(value = "List all bankslips", response = BankSlip.class)
	@GetMapping
	List<BankSlip> readBankSlips() {
		return this.bankSlipRepository
			.findAll();
	}

	@ApiOperation(value = "Pay an bankslip", response = BankSlip.class)
	@ApiResponses({
			@ApiResponse(code = 204, message = "No content"),
			@ApiResponse(code = 404, message = "Bankslip not found with the specified id")
	})
	@PostMapping("/{id}/payments")
	ResponseEntity doPayment(@PathVariable String id, @RequestBody BankSlip body){
		BankSlip bankSlip = this.bankSlipRepository.findById(id)
				.orElseThrow(BankSlipNotFoundException::new);
		bankSlip.setPaymentDate(body.getPaymentDate());
		bankSlip.setStatus(BankSlipStatus.PAID);
		this.bankSlipRepository.save(bankSlip);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Cancel an bankslip", response = BankSlip.class)
	@ApiResponses({
			@ApiResponse(code = 204, message = "Bankslip canceled"),
			@ApiResponse(code = 404, message = "Bankslip not found with the specified id")
	})
	@DeleteMapping("/{id}")
	ResponseEntity cancelPayment(@PathVariable String id){
		BankSlip bankSlip = this.bankSlipRepository.findById(id)
				.orElseThrow(BankSlipNotFoundException::new);
		bankSlip.setStatus(BankSlipStatus.CANCELED);
		this.bankSlipRepository.save(bankSlip);
		return ResponseEntity.noContent().build();
	}
}
// end::code[]
