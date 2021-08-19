package org.indiv.cambridgew.lottery.manager.common;

import org.springframework.lang.NonNull;

import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.springframework.util.Assert.isTrue;

/**
 * 别名抽样算法-抽样器
 * <p>等概分布 + 二项分布</p>
 * <p>一个奖池对应一个抽样器</p>
 *
 * @author cambridge.w
 * @link https://www.keithschwarz.com/darts-dice-coins/
 * @since 2021/8/18
 */
public class AliasSampler {

    /**
     * 概率和
     */
    private static final Double TOTAL_PROBABILITY = 1d;

    /**
     * 别名表-记录生成二项分布的补充概率的id信息
     */
    private final int[] alias;

    /**
     * 概率表-记录二项分布的概率
     */
    private final double[] probabilities;

    private final Random random = new Random();

    /**
     * 初始化抽样器
     *
     * @param initialProbabilities 概率数组
     */
    public AliasSampler(@NonNull List<Double> initialProbabilities) {
        isTrue(initialProbabilities.size() > 0, "概率数组不可为空");

        int probabilityNum = initialProbabilities.size();
        this.alias = new int[probabilityNum];
        this.probabilities = new double[probabilityNum];

        // 大小双端队列用于存放概率在原始数列中的位置
        Deque<Integer> sDeque = new ConcurrentLinkedDeque<>();
        Deque<Integer> lDeque = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < probabilityNum; i++) {
            if (initialProbabilities.get(i) * probabilityNum >= TOTAL_PROBABILITY) {
                lDeque.addLast(i);
                continue;
            }
            sDeque.addLast(i);

        }

        while (!lDeque.isEmpty() && !sDeque.isEmpty()) {
            // 用moreProbabilityIndex位置的概率填充lessProbabilityIndex位置的概率
            int lessProbabilityIndex = sDeque.removeLast();
            int moreProbabilityIndex = lDeque.removeLast();
            probabilities[lessProbabilityIndex] = initialProbabilities.get(lessProbabilityIndex) * probabilityNum;
            alias[lessProbabilityIndex] = moreProbabilityIndex;
            initialProbabilities.set(moreProbabilityIndex, initialProbabilities.get(moreProbabilityIndex) - (TOTAL_PROBABILITY / probabilityNum - initialProbabilities.get(lessProbabilityIndex)));

            if (initialProbabilities.get(moreProbabilityIndex) * probabilityNum >= TOTAL_PROBABILITY) {
                lDeque.addLast(moreProbabilityIndex);
                continue;
            }
            sDeque.addLast(moreProbabilityIndex);
        }

        while (!sDeque.isEmpty()) {
            probabilities[sDeque.removeLast()] = TOTAL_PROBABILITY;
        }

        while (!lDeque.isEmpty()) {
            probabilities[lDeque.removeLast()] = TOTAL_PROBABILITY;
        }
    }

    /**
     * 执行抽样动作
     *
     * @return 概率数组下标
     */
    public Integer sample() {
        // 等概抽样
        int column = random.nextInt(probabilities.length);
        // 二项分布抽样
        boolean coinToss = random.nextDouble() < probabilities[column];
        return coinToss ? column : alias[column];
    }


}
