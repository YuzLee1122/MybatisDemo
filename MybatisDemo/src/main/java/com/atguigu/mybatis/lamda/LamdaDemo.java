package com.atguigu.mybatis.lamda;

import java.util.Comparator;

/**
 * Created by Smexy on 2023/2/11
 *
 *  lamda表达式是简化函数式接口的创建方式。
 *
 *   lamda表达式声明的是一个函数式接口的对象。
 *
 *   函数式接口：  标注了@FunctionalInterface注解的接口。
 *                  特征： 除了从Object继承的方法外，只有一个需要实现的抽象方法！
 *
 *          为接口提供实现类:
 *                  ①使用外部类实现。
 *                          适用于高频使用的场景
 *
 *                  ②内部类
 *                          在 LamdaDemo类中，再声明一个类。
 *                          适用于仅仅在当前类中使用，绝大部分场景是在当前类中使用
 *
 *                  ③匿名内部类
 *                          仅仅使用一次，最方法！
 *
 *      ---------------------
 *          lamda语法格式： 把函数式接口所必须实现的那个抽象方法使用lamda表达式实现
 *              (参数列表) -> {方法体}
 *                 参数列表中参数的类型可以省略，由编译器推导。
 *                 参数列表中，如果只有一个参数，那么()可以省略。
 *                 方法体中，如果代码只有一行，{}可以省略。
 *                 如果代码只有一行，且是return xxx，return可以省略。
 *
 *      ------------------------
 *          方法引用是lamda表达式的一个特例。
 *                  如果要实现的接口的抽象方法的实现逻辑和某个现有的方法的实现逻辑一模一样，
 *                  可以省略编写接口抽象方法的实现逻辑，而是直接引用现有的方法！
 *
 *
 */
public class LamdaDemo
{
    public static void main(String[] args) {


        Comparator c1= new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

        System.out.println(c1.compare(1, 2));

        System.out.println("-------------用lamda表达式简化----------");

        Comparator<Integer> c2 = ( i1,  i2) -> i1.compareTo(i2);


        System.out.println(c2.compare(1, 2));

        System.out.println("-------------用方法引用简化----------");

        Comparator<Integer> c3 = (Integer x, Integer y) -> {
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        };

        System.out.println(c3.compare(1, 2));

        Comparator<Integer> c4 = Integer::compare;
        System.out.println(c4.compare(1, 2));


    }
}
