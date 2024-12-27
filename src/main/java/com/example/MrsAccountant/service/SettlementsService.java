package com.example.mrsaccountant.service;

import org.springframework.stereotype.Service;

import com.example.mrsaccountant.dto.SettlementStatusDTO;
import com.example.mrsaccountant.entity.Group;
import com.example.mrsaccountant.entity.GroupTransaction;
import com.example.mrsaccountant.entity.Settlement;
import com.example.mrsaccountant.entity.TransactionSplit;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.entity.UserGroup;
import com.example.mrsaccountant.repository.SettlementRepository;

import static java.util.stream.Collectors.toMap;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SettlementsService {

        private final GroupService groupService;

        private final SettlementRepository settlementRepository;

        public SettlementsService(GroupService groupService,
                        SettlementRepository settlementRepository) {
                this.groupService = groupService;

                this.settlementRepository = settlementRepository;

        }

        @Transactional
        public void addSettlementByGroupId(long groupId) {
                Group group = groupService.getGroupByGroupId(groupId);
                List<GroupTransaction> transactions = group.getGroupTransactions();
                List<UserGroup> users = group.getUserGroups();
                 


                List<TransactionSplit> transactionSplits = transactions.stream()
                                .filter(transaction -> transaction.getType().equals(GroupTransaction.Type.EXPENSE))
                                .flatMap(transaction -> transaction.getTransactionSplits().stream())
                                .collect(Collectors.toList());

                List<Settlement> setttlements = users.stream()
                                .map(user -> {
                                        Settlement settlement = new Settlement();
                                        User addedUser  = user.getUser();
                                        double payAmount = transactionSplits.stream()
                                                        .filter(split -> split.getUser().equals(addedUser)
                                                                        && split.getRole().equals(
                                                                                        TransactionSplit.Role.PAYER))

                                                        .mapToDouble(TransactionSplit::getAmount)
                                                        .sum();
                                        double receiveAmount = transactionSplits.stream()
                                                        .filter(split -> split.getUser().equals(addedUser)
                                                                        && split.getRole().equals(
                                                                                        TransactionSplit.Role.RECEIVER))
                                                        .mapToDouble(TransactionSplit::getAmount)
                                                        .sum();

                                        double balanceAmount = payAmount - receiveAmount;

                                        settlement.setPayAmount(payAmount);
                                        settlement.setReceiveAmount(receiveAmount);
                                        settlement.setBalanceAmount(balanceAmount);
                                        settlement.setGroup(group);
                                        settlement.setUser(addedUser);

                                        return settlement;
                                })
                                .collect(Collectors.toList());

                settlementRepository.saveAll(setttlements);

        }

        public List<Settlement> getLatestSettlement(Long groupId) {

                return settlementRepository.findByGroupId(groupId);

        }



        public List<SettlementStatusDTO> replySettlement(Long groupId) {

                List<Settlement> settlements = getLatestSettlement(groupId);
                Map<User, Double> userBalances = new HashMap<User, Double>();
                settlements.stream().forEach(settlement -> {
                        User user = settlement.getUser();

                        userBalances.put(user, settlement.getBalanceAmount());
                });

                Queue<Map.Entry<User, Double>> positiveBalances = new LinkedList<Map.Entry<User, Double>>();
                Queue<Map.Entry<User, Double>> negativeBalances = new LinkedList<Map.Entry<User, Double>>();

                Map<User, Double> sortedUserBalances = userBalances
                                .entrySet()
                                .stream()
                                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                .collect(
                                                toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                                                LinkedHashMap::new));

                sortedUserBalances.entrySet().stream()
                                .filter(entry -> entry.getValue() >= 0)
                                .forEach(positiveBalances::add);
                sortedUserBalances.entrySet().stream()
                                .filter(entry -> entry.getValue() < 0)
                                .forEach(entry -> negativeBalances.add(entry));

                Stack<Map.Entry<User, Double>> reversedNegativeBalances = new Stack<>();
                while (!negativeBalances.isEmpty()) {
                        reversedNegativeBalances.push(negativeBalances.poll());
                }

                while (!reversedNegativeBalances.isEmpty()) {
                        negativeBalances.add(reversedNegativeBalances.pop());
                }

                List<SettlementStatusDTO> settlementStatuses = new ArrayList<>();

                while (!positiveBalances.isEmpty()) {
                        if (positiveBalances.peek().getValue() >= Math.abs(negativeBalances.peek().getValue())) {

                                Double replyAmount = Math.abs(negativeBalances.peek().getValue());

                                positiveBalances.peek().setValue(positiveBalances.peek().getValue()
                                                + negativeBalances.peek().getValue());

                                negativeBalances.peek().setValue(0.0);

                                SettlementStatusDTO settlementStatus = new SettlementStatusDTO(
                                                negativeBalances.peek().getKey().getUsername(),
                                                positiveBalances.peek().getKey().getUsername(),
                                                replyAmount);

                                settlementStatuses.add(settlementStatus);

                                negativeBalances.poll();
                                if (negativeBalances.isEmpty()) {
                                        break;
                                }
                        }
                         if (positiveBalances.peek().getValue() < Math.abs(negativeBalances.peek().getValue())) {
                                Double replyRemainAmount = Math.abs(positiveBalances.peek().getValue());
                                negativeBalances.peek().setValue(negativeBalances.peek().getValue()
                                                + positiveBalances.peek().getValue());
                                positiveBalances.peek().setValue(0.0);
                                positiveBalances.poll();

                                SettlementStatusDTO settlementStatus = new SettlementStatusDTO(
                                                negativeBalances.peek().getKey().getUsername(),
                                                positiveBalances.peek().getKey().getUsername(),
                                                replyRemainAmount);

                                settlementStatuses.add(settlementStatus);

                        }

                }
                return settlementStatuses;
        }

}
