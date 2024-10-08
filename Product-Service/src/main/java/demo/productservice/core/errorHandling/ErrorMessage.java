package demo.productservice.core.errorHandling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class ErrorMessage {
    private Date timestamp;
    private String message;
}
