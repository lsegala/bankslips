package bookmarks;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang.time.DateUtils;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Optional;

@Entity
@EntityListeners({BankSlipListener.class})
public class BankSlip {

    @Id
    @GeneratedValue
    private Long id;

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

    private BankSlip() { } // JPA only

    public BankSlip( final String customer, final Calendar dueDate, final BigDecimal totalInCents) {
        this.customer = customer;
        this.dueDate = dueDate;
        this.totalInCents = totalInCents;
    }

    public Long getId() {
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
                    BigDecimal j05 = new BigDecimal(0.005D);
                    BigDecimal j1 = new BigDecimal(0.01D);
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
}
