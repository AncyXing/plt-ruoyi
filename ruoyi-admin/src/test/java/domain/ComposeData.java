package domain;

import com.example.pltool.domain.entity.Lexicon;
import com.example.pltool.domain.entity.LexiconWord;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.domain.entity.WordSentence;
import lombok.Data;

import java.util.List;

/**
 * @date 2024/11/14
 */
@Data
public class ComposeData {
    private Word word;
    private Lexicon lexicon;
    private List<WordSentence> wordSentence;
    private LexiconWord lexiconWord;
}
