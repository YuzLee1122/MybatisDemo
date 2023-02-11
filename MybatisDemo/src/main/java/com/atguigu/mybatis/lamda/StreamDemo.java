package com.atguigu.mybatis.lamda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Smexy on 2023/2/11
 */
public class StreamDemo
{
    public static void main(String[] args) {

        //从 collection转为stream
        List<Integer> list = Arrays.asList(1, 2, 3);
        Stream<Integer> s1 = list.stream();

        //从 array转为stream
        String [] array = new String[]{"a","b"};
        Stream<String> s2 = Arrays.stream(array);

        //基于元素获取Stream
        Stream<Integer> s3 = Stream.of(1, 2, 3, 4, 5, 6);

        // 对s3进行每个元素+10，之后取奇数。
       /* List<Integer> result = s3
            .map(new Function<Integer, Integer>()
            {
                @Override
                public Integer apply(Integer in) {
                    return in + 10;
                }
            })
            .filter(new Predicate<Integer>()
            {
                //验证输入是否符合要求 将返回true的元素留下
                @Override
                public boolean test(Integer in) {
                    return in % 2 == 1;
                }
            })
            //调用终止操作(行动算子)
            .collect(Collectors.toList());*/


        //lamda
        List<Integer> result2 = s3.map(i -> i + 10)
                                  .filter(i -> i % 2 == 1)
                                  .collect(Collectors.toList());

        System.out.println(result2);

    }
}
