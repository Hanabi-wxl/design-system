package edu.dlu.bysj.base.model.enums;

/**
 * @author XiangXinGang
 * @date 2021/11/14 14:58
 */
public enum FractionalSegmentEnum {
    EXCELLENT(90, Integer.MAX_VALUE, "A"),
    GOODE(80, 89, "B"),
    MEDIUM(70, 79, "C"),
    POOR(60, 69, "D"),
    FAILED(60, Integer.MIN_VALUE, "F");


    private Integer scoreUpperBound;
    private Integer scoreLowerBound;
    private String Mapping;

    FractionalSegmentEnum(Integer scoreUpperBound, Integer scoreLowerBound, String mapping) {
        this.scoreUpperBound = scoreUpperBound;
        this.scoreLowerBound = scoreLowerBound;
        Mapping = mapping;
    }

    public Integer getScoreUpperBound() {
        return scoreUpperBound;
    }

    public void setScoreUpperBound(Integer scoreUpperBound) {
        this.scoreUpperBound = scoreUpperBound;
    }

    public Integer getScoreLowerBound() {
        return scoreLowerBound;
    }

    public void setScoreLowerBound(Integer scoreLowerBound) {
        this.scoreLowerBound = scoreLowerBound;
    }

    public String getMapping() {
        return Mapping;
    }

    public void setMapping(String mapping) {
        Mapping = mapping;
    }

    public static String getScoreMapping(Integer score) {
        FractionalSegmentEnum[] values = FractionalSegmentEnum.values();
        for (FractionalSegmentEnum element : values) {
            if (element.getScoreLowerBound().compareTo(score) <= 0 && element.getScoreUpperBound().compareTo(score) >= 0) {
                return element.getMapping();
            }
        }
        return null;
    }
}
