package model;

public enum Status {
    Green, Red, Yellow;

//        Green("Green"),
//        Red("Red"),
//        Yellow("Yellow");

//        private final String value;
//
//        Status (String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public static Status toString(String text) {
//            for (Status stat : Status.values()) {
//                if (stat.value.equalsIgnoreCase(text)){
//                    return stat;
//                }
//            }
//            throw new IllegalArgumentException("Green, Red, Yellow 중에서 입력해 주십시오.");
//        }
}