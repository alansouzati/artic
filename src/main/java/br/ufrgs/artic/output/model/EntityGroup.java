package br.ufrgs.artic.output.model;

import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.model.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the entity groups after the execution of the post-processing algorithms
 */
public class EntityGroup {

    private List<CRFWord> crfWords = new ArrayList<>();
    private EntityDimension dimension;

    public String getText() {
        if (crfWords != null) {
            StringBuilder text = new StringBuilder();
            for (CRFWord crfWord : crfWords) {
                text.append(crfWord.getWord().getContent()).append(" ");
            }
            return text.toString().trim();
        }

        return null;
    }

    public List<CRFWord> getCrfWords() {
        return crfWords;
    }

    public EntityDimension getDimension() {
        return dimension;
    }

    public void setDimension(EntityDimension dimension) {
        this.dimension = dimension;
    }

    public static EntityGroup parse(CRFWord crfWord) {

        Word word = crfWord.getWord();

        EntityGroup entityGroup = new EntityGroup();
        entityGroup.getCrfWords().add(crfWord);
        entityGroup.setDimension(new EntityDimension(word.getLeft(),
                word.getRight(), word.getTop(), word.getBottom()));

        return entityGroup;
    }
}
