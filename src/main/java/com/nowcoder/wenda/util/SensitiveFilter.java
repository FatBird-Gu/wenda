package com.nowcoder.wenda.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符
    private  static final String REPLACEMENT = "***";

    // 根节点
    private  TrieNode root = new TrieNode();

    @PostConstruct
    public void init(){
        try{
            URL url = Thread.currentThread().getContextClassLoader().getResource("sensitive-words.txt");
            String file = URLDecoder.decode(url.getFile(),"utf-8");
            FileInputStream fis = new FileInputStream(file);

            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String keyword;
            while((keyword = reader.readLine())!=null){
                // 敏感词添加到前缀树
                this.addKeyword(keyword);
            }
            reader.close();
            fis.close();
        } catch (IOException e) {
            System.out.println(this.getClass());
            logger.error("加载敏感词文件失败:"+e.getMessage());
        }
    }

    // 敏感词添加到前缀树中
    private void addKeyword(String keyword){
        TrieNode node = root;
        for (int i = 0;i<keyword.length();i++){
            char c = keyword.charAt(i);
            if (node.getSubNode(c) == null){
                // 初始化子节点
                node.addSubNode(c,new TrieNode());
            }

            // 指向子节点
            node = node.getSubNode(c);

            // 设置结束标识
            if (i == keyword.length()-1){
                node.setKeywordKey(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 带过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }

        // 指针 1
        TrieNode tempNode = root;
        // 指针 2
        int begin = 0;
        // 指针 3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while(position < text.length()){
            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)){
                // 若指针1 处于根节点,将此符号计入结果，让指针2向下走一步
                if (tempNode == root){
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或者中间 指针3 都向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null){
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                ++begin;
                position = begin;
                // 重新指向根节点
                tempNode = root;
            } else if(tempNode.isKeywordKey()){
                // 发现敏感词，将begin-position的替换
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin = ++position;
                //重新指向根节点
                tempNode = root;
            }else{
                //检查下一个字符
                position ++;
            }
        }
        // 将最后一批字符记录结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    // 判断字符是否为符号
    private boolean isSymbol(Character c){
        // 0x2e0-0x9fff 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 前缀树
    private class TrieNode{
        // 关键词结束标志
        private  boolean isKeywordKey = false;

        // 子节点(key是下级结点字符，value为下级节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordKey() {
            return isKeywordKey;
        }

        public void setKeywordKey(boolean keywordKey) {
            isKeywordKey = keywordKey;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }
    }



}
