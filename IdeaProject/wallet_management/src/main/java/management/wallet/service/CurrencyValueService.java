package management.wallet.service;

import management.wallet.DAO.CurrencyValueDAO;
import management.wallet.model.CurrencyValue;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CurrencyValueService {
    CurrencyValueDAO currencyValueDAO;

    public BigDecimal findMedianByDate(LocalDate date) {
        List<CurrencyValue> currencyValues = currencyValueDAO.findAllByDate(date);
        int mediumIndex;
        int size = currencyValues.size();
        if (size % 2 == 0) {
            mediumIndex = size / 2;
            return (currencyValues.get(mediumIndex).getExchangeValue()
                    .add(currencyValues.get(mediumIndex - 1).getExchangeValue()))
                    .divide(new BigDecimal(2));
        } else {
            mediumIndex = (size - 1) / 2;
            return currencyValues.get(mediumIndex).getExchangeValue();
        }
    }
}
