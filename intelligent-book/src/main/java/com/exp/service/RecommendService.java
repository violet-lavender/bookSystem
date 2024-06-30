package com.exp.service;

import com.exp.dto.BookGrade;
import com.exp.dto.RecommendParam;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    // TODO Recommend books for user

    public List<Integer> userBasedRecommendation(RecommendParam recommendParam, int k, int n) {
        int targetUserId = recommendParam.getUserId();
        List<BookGrade> bookGradeList = recommendParam.getBookGradeList();
        List<Integer> bookIds = recommendParam.getBookIds();

        // 获取所有用户ID并去重
        Set<Integer> userIds = bookGradeList.stream()
                .map(BookGrade::getUserId)
                .collect(Collectors.toSet());
        userIds.add(targetUserId);

        // 创建用户和书籍索引映射
        List<Integer> userIdList = new ArrayList<>(userIds);
        Map<Integer, Integer> userIdToIndex = new HashMap<>();
        for (int i = 0; i < userIdList.size(); i++) {
            userIdToIndex.put(userIdList.get(i), i);
        }

        Map<Integer, Integer> bookIdToIndex = new HashMap<>();
        for (int i = 0; i < bookIds.size(); i++) {
            bookIdToIndex.put(bookIds.get(i), i);
        }

        // 创建评分矩阵
        RealMatrix ratingMatrix = new BlockRealMatrix(userIds.size(), bookIds.size());
        for (BookGrade bookGrade : bookGradeList) {
            int userIndex = userIdToIndex.get(bookGrade.getUserId());
            int bookIndex = bookIdToIndex.get(bookGrade.getBookId());
            ratingMatrix.setEntry(userIndex, bookIndex, bookGrade.getGrade());
        }

        // 计算用户相似度矩阵
        RealMatrix similarityMatrix = cosineSimilarity(ratingMatrix);

        // 获取目标用户的相似度向量，并排除自己与自己的相似度（设为0）
        int targetUserIndex = userIdToIndex.get(targetUserId);
        double[] targetSimilarity = similarityMatrix.getRow(targetUserIndex);
        targetSimilarity[targetUserIndex] = 0;

        // 找到相似度最高的k个用户的索引
        int[] similarUsersIndices = argsort(targetSimilarity, k);

        // 初始化推荐字典，存储每本书的累计评分
        Map<Integer, Double> recommendScores = new HashMap<>();

        // 遍历每个相似用户
        for (int userIdx : similarUsersIndices) {
            double similarityScore = targetSimilarity[userIdx];

            // 遍历目标用户未评分的每本书
            for (int itemIdx = 0; itemIdx < ratingMatrix.getColumnDimension(); itemIdx++) {
                if (ratingMatrix.getEntry(targetUserIndex, itemIdx) == 0) {  // 目标用户未评分
                    recommendScores.put(itemIdx, recommendScores.getOrDefault(itemIdx, 0.0) + similarityScore * ratingMatrix.getEntry(userIdx, itemIdx));
                }
            }
        }

        // 对推荐字典按评分值降序排序
        List<Map.Entry<Integer, Double>> sortedRecommendations = new ArrayList<>(recommendScores.entrySet());
        sortedRecommendations.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        // 提取排名前n的书籍
        return sortedRecommendations.stream()
                .limit(n)
                .map(Map.Entry::getKey)
                .map(bookIds::get)
                .collect(Collectors.toList());
    }

    private RealMatrix cosineSimilarity(RealMatrix matrix) {
        int rows = matrix.getRowDimension();
        RealMatrix similarityMatrix = new BlockRealMatrix(rows, rows);

        for (int i = 0; i < rows; i++) {
            for (int j = i; j < rows; j++) {
                double similarity = calculateCosineSimilarity(matrix.getRowVector(i), matrix.getRowVector(j));
                similarityMatrix.setEntry(i, j, similarity);
                similarityMatrix.setEntry(j, i, similarity);
            }
        }
        return similarityMatrix;
    }

    private double calculateCosineSimilarity(RealVector vectorA, RealVector vectorB) {
        double dotProduct = 0.0;
        for (int i = 0; i < vectorA.getDimension(); i++) {
            dotProduct += vectorA.getEntry(i) * vectorB.getEntry(i);
        }
        double normA = vectorA.getNorm();
        double normB = vectorB.getNorm();
        return dotProduct / (normA * normB);
    }

    private int[] argsort(double[] array, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((i1, i2) -> Double.compare(array[i2], array[i1]));
        for (int i = 0; i < array.length; i++) {
            pq.offer(i);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        int[] indices = new int[k];
        int idx = k - 1;
        while (!pq.isEmpty() && idx >= 0) {
            indices[idx--] = pq.poll();
        }
        return indices;
    }
}


