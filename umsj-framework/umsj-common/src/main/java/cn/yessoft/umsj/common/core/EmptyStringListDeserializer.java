package cn.yessoft.umsj.common.core;

import cn.yessoft.umsj.common.utils.BaseUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmptyStringListDeserializer extends JsonDeserializer<List<String>> {

    /**
     * 反序列化 JSON 数组，处理空字符串并将其转换为空列表。
     *
     * @param p    JSON 解析器。
     * @param ctxt 反序列化上下文。
     * @return 包含反序列化字符串的 List<String>。
     * @throws IOException 如果在反序列化过程中发生 I/O 错误。
     */
    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 读取整个 JSON 节点
        JsonNode node = p.readValueAsTree();
        // 检查节点是否为 null、缺失或不是数组
        if (node == null || node.isNull() || node.isMissingNode() || !node.isArray()) {
            // 对于 null、缺失或非数组情况，返回空列表
            return new ArrayList<>();
        } else {
            // 处理数组中的每个元素
            List<String> values = new ArrayList<>();
            for (JsonNode element : node) {
                // 检查元素是否为文本节点
                if (element.isTextual() && BaseUtils.isNotEmpty(element.textValue())) {
                    // 将文本值添加到列表中
                    values.add(element.textValue());
                }
            }
            // 返回反序列化字符串的列表
            return values;
        }
    }
}
