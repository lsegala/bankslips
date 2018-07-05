package bankslips;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Optional;

@Entity
@EntityListeners({BankSlipListener.class})
public class BankSlip {

    @Id
    private String id;

    private String customer;

    @JsonProperty("due_date")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
    private Calendar dueDate;

    @JsonProperty("payment_date")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
    private Calendar paymentDate;

    @JsonProperty("total_in_cents")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal totalInCents;

    @Enumerated(EnumType.STRING)
    private BankSlipStatus status;

    private BankSlip() { } // JPA only

    public BankSlip( final String customer, final Calendar dueDate, final BigDecimal totalInCents) {
        this.customer = customer;
        this.dueDate = dueDate;
        this.totalInCents = totalInCents;
        this.status = BankSlipStatus.PENDING;
    }

    public BankSlip(String customer, Calendar dueDate, BigDecimal totalInCents, BankSlipStatus status) {
        this.customer = customer;
        this.dueDate = dueDate;
        this.totalInCents = totalInCents;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public BigDecimal getTotalInCents() {
        return Optional.ofNullable(totalInCents).map(t ->
            t.add(
                Optional.ofNullable(dueDate)
                .filter(d -> Duration.between(d.toInstant(), Instant.now()).toDays() > 0)
                .map(d -> {
                    long p = Duration.between(d.toInstant(), Instant.now()).toDays();
                    BigDecimal j05 = BigDecimal.valueOf(0.005D);
                    BigDecimal j1 = BigDecimal.valueOf(0.01D);
                    return t.multiply(p <= 10? j05 : j1).multiply(new BigDecimal(p));
                }).orElse(BigDecimal.ZERO))
            ).orElse(null);
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public Calendar getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Calendar paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BankSlipStatus getStatus() {
        return status;
    }

    public void setStatus(BankSlipStatus status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }
}
