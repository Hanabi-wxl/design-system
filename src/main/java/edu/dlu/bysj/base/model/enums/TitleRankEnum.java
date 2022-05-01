package edu.dlu.bysj.base.model.enums;

/**
 * @author XiangXinGang
 * @date 2021/10/16 22:03
 */
public enum TitleRankEnum {
    /**
     * 1- Advanced
     * 2- Deputy Senior
     * 3- Intermediate
     * 4- Elementary
     */
    ADVANCED(1,"高级"),
    DEPUTY_SENIOR(2,"副高级"),
    INTERMEDIATE(3,"中级"),
    ELEMENTARY(4,"初级");

    TitleRankEnum(Integer rankNumber, String description) {
        this.rankNumber = rankNumber;
        this.description = description;
    }

    private Integer rankNumber;
    private String description;

    public Integer getRankNumber() {
        return rankNumber;
    }

    public void setRankNumber(Integer rankNumber) {
        this.rankNumber = rankNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String backDescription(Integer number) {
        TitleRankEnum[] values = TitleRankEnum.values();
        for (TitleRankEnum element: values) {
            if (element.getRankNumber().equals(number) == true) {
                return element.getDescription();
            };
        }
        return  "";
    }
}
