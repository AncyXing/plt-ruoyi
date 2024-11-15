package domain;

import lombok.Data;

import java.util.List;

/**
 * @author yangxing
 * @date 2024/11/14
 */
@Data
public class ParseData {
    private String wordHead;
    private String lexicon;
    private List<SentenceData> sentences;
    private List<TransData> trans;
}
