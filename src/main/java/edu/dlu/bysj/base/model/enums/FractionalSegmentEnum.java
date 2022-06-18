package edu.dlu.bysj.base.model.enums;

/**
 * @author XiangXinGang
 * @date 2021/11/14 14:58
 */
public enum FractionalSegmentEnum {
    EXCELLENT(Integer.MAX_VALUE, 90, "A"),
    GOODE(89, 80, "B"),
    MEDIUM(79, 70, "C"),
    POOR(69, 60, "D"),
    FAILED(59, Integer.MIN_VALUE, "F");


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
