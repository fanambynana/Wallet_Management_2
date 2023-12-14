package management.wallet.service;

import management.wallet.DAO.TransferHistoryDAO;
import management.wallet.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferHistoryService {
    TransferHistoryDAO transferHistoryDAO;

    public List<Account> findByIntervalReturnDebitAccount(LocalDateTime from, LocalDateTime to) {
        return transferHistoryDAO.findByIntervalReturnDebitAccount(from, to);
    }
    public  List<Account> findByIntervalReturnCreditAccount(LocalDateTime from, LocalDateTime to) {
        return transferHistoryDAO.findByIntervalReturnCreditAccount(from, to);
    }
    public List<BigDecimal> findByIntervalReturnTransferAmount(LocalDateTime from, LocalDateTime to) {
        return transferHistoryDAO.findByIntervalReturnTransferAmount(from, to);
    }
    public List<LocalDateTime> findByIntervalReturnTransferDateTime(LocalDateTime from, LocalDateTime to) {
        return transferHistoryDAO.findByIntervalReturnTransferDateTime(from, to);
    }
}
