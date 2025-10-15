package graphql.mapper.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToClassGenerator {

    public static String generateClassFromJson(String className, String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        StringBuilder sb = new StringBuilder();
        sb.append("import lombok.Data;\n\n");
        sb.append("@Data\n");
        sb.append("public class ").append(className).append(" {\n");
        buildClass(sb, root, "    ");
        sb.append("}\n");
        return sb.toString();
    }

    private static void buildClass(StringBuilder sb, JsonNode node, String indent) {
        node.fieldNames().forEachRemaining(field -> {
            JsonNode child = node.get(field);
            String fieldName = field;
            String type;

            if (child.isObject()) {
                String innerClassName = capitalize(fieldName);
                sb.append(indent).append("private ").append(innerClassName)
                        .append(" ").append(fieldName).append(";\n");

                // Gera classe interna
                sb.append("\n").append(indent).append("@Data\n");
                sb.append(indent).append("public static class ").append(innerClassName).append(" {\n");
                buildClass(sb, child, indent + "    ");
                sb.append(indent).append("}\n\n");
            } else if (child.isArray()) {
                if (child.size() > 0 && child.get(0).isObject()) {
                    String innerClassName = capitalize(fieldName) + "Item";
                    sb.append(indent).append("private java.util.List<").append(innerClassName).append("> ")
                            .append(fieldName).append(";\n");

                    sb.append("\n").append(indent).append("@Data\n");
                    sb.append(indent).append("public static class ").append(innerClassName).append(" {\n");
                    buildClass(sb, child.get(0), indent + "    ");
                    sb.append(indent).append("}\n\n");
                } else {
                    type = getTypeFromValue(child.size() > 0 ? child.get(0) : null);
                    sb.append(indent).append("private java.util.List<").append(type).append("> ")
                            .append(fieldName).append(";\n");
                }
            } else {
                type = getTypeFromValue(child);
                sb.append(indent).append("private ").append(type)
                        .append(" ").append(fieldName).append(";\n");
            }
        });
    }

    private static String getTypeFromValue(JsonNode value) {
        if (value == null || value.isNull()) return "Object";
        if (value.isTextual()) return "String";
        if (value.isInt()) return "Integer";
        if (value.isLong()) return "Long";
        if (value.isDouble() || value.isFloat() || value.isBigDecimal()) return "Double";
        if (value.isBoolean()) return "Boolean";
        return "Object";
    }

    private static String capitalize(String name) {
        if (name == null || name.isEmpty()) return name;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public static void main(String[] args) throws Exception {
        String json = """
        {
            "id": 1,
            "name": "Adamis",
            "active": true,
            "address": {
                "street": "Rua X",
                "city": "Belo Horizonte"
            },
            "tags": ["java", "spring", "angular"]
        }
        """;

        System.out.println(generateClassFromJson("User", json));
    }
}
