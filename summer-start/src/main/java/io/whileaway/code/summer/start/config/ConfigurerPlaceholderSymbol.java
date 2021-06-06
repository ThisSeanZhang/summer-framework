package io.whileaway.code.summer.start.config;

import io.whileaway.code.summer.util.StringUtils;

import java.util.Optional;

public final class ConfigurerPlaceholderSymbol {

    /**
     * Default placeholder prefix: {@value}
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * Default placeholder suffix: {@value}
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /**
     * Default value separator: {@value}
     */
    public static final String DEFAULT_VALUE_SEPARATOR = ":";

    private ConfigurerPlaceholderSymbol(){}

    public static Optional<Tuple<String, String>> getGlobalKeyAndValueTuple(String needSplit) {
        if (StringUtils.isEmptyStr(needSplit)) return Optional.empty();
        if (!needSplit.startsWith(DEFAULT_PLACEHOLDER_PREFIX) || !needSplit.endsWith(DEFAULT_PLACEHOLDER_SUFFIX)) return Optional.empty();
        String[] split = needSplit.trim().replaceAll("^\\$\\{|}$", "").split(DEFAULT_VALUE_SEPARATOR);
        return Optional.of(
                new Tuple<>(
                        split[0],
                        split.length < 2 ? "" : split[1]
                )
        );
    }


//    public static void main(String[] args) {
//        System.out.println("${aaa${asdada}a}".replaceAll("^\\$\\{|}$", ""));
//    }
}
