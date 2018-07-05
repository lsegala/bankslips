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
package bookmarks;

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
@RequestMapping("/bankslips")
class BankSlipRestController {

	private final BankSlipRepository bankSlipRepository;

	@Autowired
	BankSlipRestController(BankSlipRepository bankSlipRepository) {
		this.bankSlipRepository = bankSlipRepository;
	}

	@GetMapping("/{id}")
	BankSlip readBankSlip(@PathVariable String id) {
		return this.bankSlipRepository.findById(id)
				.orElseThrow(BankSlipNotFoundException::new);
	}

	@PostMapping
	ResponseEntity<BankSlip> add(@RequestBody BankSlip bankSlip) {
		return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.bankSlipRepository.save(bankSlip));
	}

	@GetMapping
	List<BankSlip> readBankSlips() {
		return this.bankSlipRepository
			.findAll();
	}

	@PostMapping("/{id}/payments")
	ResponseEntity doPayment(@PathVariable String id, @RequestBody BankSlip body){
		BankSlip bankSlip = this.bankSlipRepository.findById(id)
				.orElseThrow(BankSlipNotFoundException::new);
		bankSlip.setPaymentDate(body.getPaymentDate());
		bankSlip.setStatus(BankSlipStatus.PAID);
		this.bankSlipRepository.save(bankSlip);
		return ResponseEntity.noContent().build();
	}

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
