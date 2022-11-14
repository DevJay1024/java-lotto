package lotto.client;


import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lotto.InvalidInputMessage;
import lotto.Lotto;
import lotto.LottoNumber;
import lotto.LottoPlace;

public class ConsoleUserInterface {

    private final static String MSG_REQUEST_PURCHASE_AMOUNT = "구입금액을 입력해 주세요.";
    private final static String MSG_SHOW_PURCHASE_AMOUNT = "개를 구매했습니다.";
    private final static String MSG_REQUEST_WINNING_NUMBERS = "당첨 번호를 입력해 주세요.";
    private final static String FORMAT_SPLITTER_WINNING_NUMBERS = ",";
    private final static String MSG_REQUEST_BONUS_NUMBER = "보너스 번호를 입력해 주세요.";
    private final static String MSG_SHOW_LOTTO_STATISTICS = "당첨 통계\n---";
    private final static String MSG_SHOW_COUNT_WINNING_BY_PLACE = "{0} ({1}) - {2}개";
    private final static String MSG_SHOW_TOTAL_MARGIN_RATE = "총 수익률은 {0}%입니다.";

    public ConsoleUserInterface() {
    }

    protected void output(String message) {
        System.out.println(message);
    }

    protected String input() {
        String inputLine;
        try {
            inputLine = Console.readLine();
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    InvalidInputMessage.ERR_DEFAULT + InvalidInputMessage.ERR_EMPTY_INPUT
            );
        }
        return inputLine;
    }

    public int requestPurchaseAmount() {
        output(MSG_REQUEST_PURCHASE_AMOUNT);
        String input = input();
        validatePurchasingAmount(input);
        return Integer.parseInt(input);
    }

    private void validateAs_JavaInteger(String input) {
        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    InvalidInputMessage.ERR_DEFAULT + InvalidInputMessage.ERR_NOT_JAVA_INTEGER
            );
        }
    }

    private void validatePurchasingAmount(String input) {
        validateAs_JavaInteger(input);
        if (Integer.parseInt(input) % Lotto.PRICE != 0) {
            throw new IllegalArgumentException(
                    InvalidInputMessage.ERR_DEFAULT + InvalidInputMessage.ERR_MONEY_UNIT
            );
        }
    }

    public void showPurchasedResult(List<Lotto> lottos) {
        output(lottos.size() + MSG_SHOW_PURCHASE_AMOUNT);
        for (Lotto lotto : lottos) {
            showPurchasedLotto(lotto);
        }
    }

    private void showPurchasedLotto(Lotto lotto) {
        List<Integer> convertedNumbers = lotto.getNumbers().stream()
                .map(LottoNumber::getNumber)
                .collect(Collectors.toList());
        output(convertedNumbers.toString());
    }

    public List<Integer> requestWinningNumbers() {
        output(MSG_REQUEST_WINNING_NUMBERS);
        String input = input();
        validateWinningString(input);
        return Arrays.stream(input.split(FORMAT_SPLITTER_WINNING_NUMBERS))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private void validateWinningString(String input) {
        List<String> rawNumbers = List.of(input.split(FORMAT_SPLITTER_WINNING_NUMBERS));
        if(rawNumbers.size() != Lotto.NUMBERS_COUNT)
            throw new IllegalArgumentException(
                    InvalidInputMessage.ERR_DEFAULT + InvalidInputMessage.ERR_WINNING_NUMBER_FORMAT
            );
        for (String rawNumber : rawNumbers) {
            validateAs_JavaInteger(rawNumber);
        }
    }

    public int requestBonusNumber() {
        output(MSG_REQUEST_BONUS_NUMBER);
        String input = input();
        validateAs_JavaInteger(input);
        return Integer.parseInt(input);
    }

    public void showLottoStatistics(Map<LottoPlace, Integer> records, double marginRate) {
        List<LottoPlace> lottoPlaces = List.of(LottoPlace.values());

        for (LottoPlace place : lottoPlaces) {
            if (place.equals(LottoPlace.NONE)) {
                continue;
            }
            output(String.format(
                    MSG_SHOW_COUNT_WINNING_BY_PLACE,
                    place.getInfo(),
                    place.getPrizeMoney(),
                    records.get(place))
            );
        }
        output(String.format(MSG_SHOW_TOTAL_MARGIN_RATE, marginRate));
    }
}
