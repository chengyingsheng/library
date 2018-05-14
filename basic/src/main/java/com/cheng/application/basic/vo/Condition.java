package com.cheng.application.basic.vo;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 条件
 * Created by cheshun on 2016/5/15.
 */
public class Condition implements Serializable {

    /**
     * 运算符
     */
    public enum Operator {

        /**
         * 多表关联
         */
        on("="),

        /**
         * 或
         */
        or("OR"),

        /**
         * 等于
         */
        eq("="),

        /**
         * 不等于
         */
        ne("!="),

        /**
         * 大于
         */
        gt(">"),

        /**
         * 小于
         */
        lt("<"),

        /**
         * 大于等于
         */
        ge(">="),

        /**
         * 小于等于
         */
        le("<="),

        /**
         * 相似
         */
        like("LIKE"),

        /**
         * 包含
         */
        in("IN"),

        /**
         * 为Null
         */
        isNull("IS NULL"),

        /**
         * 不为Null
         */
        isNotNull("IS NOT NULL");

        private String value;

        Operator(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        /**
         * 从String中获取Operator
         *
         * @param value 值
         * @return String对应的operator
         */
        public static Operator fromString(String value) {
            return Operator.valueOf(value.toLowerCase());
        }
    }

    /**
     * 属性
     */
    private String property;

    /**
     * 别名
     */
    private String alias;

    /**
     * 运算符
     */
    private Operator operator;

    /**
     * 值
     */
    private Object value;

    /**
     * 或条件
     */
    private List<Condition> orList;

    private Condition() {
    }

    /**
     * 初始化一个新创建的Condition对象
     *
     * @param property 属性
     * @param operator 运算符
     * @param value    值
     */
    private Condition(String property, String alias, Operator operator, Object value) {
        this.property = property;
        this.alias = alias;
        this.operator = operator;
        this.value = value;
    }

    /**
     * 返回多表关联条件,只有在多表查询的时候使用
     * 表A的别名a,表B的别名b
     * 使用关联条件时如下
     * on("a.xxx", "b.yyy")
     *
     * @param lProperty 左字段
     * @param rProperty 右字段
     */
    public static Condition on(String lProperty, String rProperty) {
        return new Condition(lProperty, "", Operator.on, rProperty);
    }

    /**
     * 返回或条件
     *
     * @param conditions 多个或条件
     * @return 或条件
     */
    public static Condition or(Condition... conditions) {
        Condition condition = new Condition();
        condition.operator = Operator.or;
        condition.orList = Lists.newArrayList();
        for (Condition c : conditions) {
            if (c.getOperator() != Operator.or) {
                condition.orList.add(c);
            }
        }
        return condition;
    }

    /**
     * 返回等于条件
     *
     * @param property 属性
     * @param alias    别名
     * @param value    值
     * @return 等于条件
     */
    public static Condition eq(String property, String alias, Object value) {
        return new Condition(property, alias, Operator.eq, value);
    }

    public static Condition eq(String property, Object value) {
        return new Condition(property, "", Operator.eq, value);
    }

    /**
     * 返回等于条件
     *
     * @param property   属性
     * @param alias      别名
     * @param value      值
     * @param ignoreCase 忽略大小写
     * @return 等于条件
     */
    public static Condition eq(String property, String alias, Object value, boolean ignoreCase) {
        if (ignoreCase && value instanceof String) {
            return new Condition(property, alias, Operator.eq, ((String) value).toLowerCase());
        }
        return new Condition(property, alias, Operator.eq, value);
    }

    public static Condition eq(String property, Object value, boolean ignoreCase) {
        if (ignoreCase && value instanceof String) {
            return new Condition(property, "", Operator.eq, ((String) value).toLowerCase());
        }
        return new Condition(property, "", Operator.eq, value);
    }

    /**
     * 返回不等于条件
     *
     * @param property 属性
     * @param alias    别名
     * @param value    值
     * @return 不等于条件
     */
    public static Condition ne(String property, String alias, Object value) {
        return new Condition(property, alias, Operator.ne, value);
    }

    public static Condition ne(String property, Object value) {
        return new Condition(property, "", Operator.ne, value);
    }

    /**
     * 返回不等于条件
     *
     * @param property   属性
     * @param alias      别名
     * @param value      值
     * @param ignoreCase 忽略大小写
     * @return 不等于条件
     */
    public static Condition ne(String property, String alias, Object value, boolean ignoreCase) {
        if (ignoreCase && value instanceof String) {
            return new Condition(property, alias, Operator.ne, ((String) value).toLowerCase());
        }
        return new Condition(property, alias, Operator.ne, value);
    }

    public static Condition ne(String property, Object value, boolean ignoreCase) {
        if (ignoreCase && value instanceof String) {
            return new Condition(property, "", Operator.ne, ((String) value).toLowerCase());
        }
        return new Condition(property, "", Operator.ne, value);
    }

    /**
     * 返回大于条件
     *
     * @param property 属性
     * @param alias    别名
     * @param value    值
     * @return 大于条件
     */
    public static Condition gt(String property, String alias, Object value) {
        return new Condition(property, alias, Operator.gt, value);
    }

    public static Condition gt(String property, Object value) {
        return new Condition(property, "", Operator.gt, value);
    }

    /**
     * 返回小于条件
     *
     * @param property 属性
     * @param alias    别名
     * @param value    值
     * @return 小于条件
     */
    public static Condition lt(String property, String alias, Object value) {
        return new Condition(property, alias, Operator.lt, value);
    }

    public static Condition lt(String property, Object value) {
        return new Condition(property, "", Operator.lt, value);
    }

    /**
     * 返回大于等于条件
     *
     * @param property 属性
     * @param alias    别名
     * @param value    值
     * @return 大于等于条件
     */
    public static Condition ge(String property, String alias, Object value) {
        return new Condition(property, alias, Operator.ge, value);
    }

    public static Condition ge(String property, Object value) {
        return new Condition(property, "", Operator.ge, value);
    }

    /**
     * 返回小于等于条件
     *
     * @param property 属性
     * @param alias    别名
     * @param value    值
     * @return 小于等于条件
     */
    public static Condition le(String property, String alias, Object value) {
        return new Condition(property, alias, Operator.le, value);
    }

    public static Condition le(String property, Object value) {
        return new Condition(property, "", Operator.le, value);
    }

    /**
     * 返回相似条件
     *
     * @param property 属性
     * @param alias    别名
     * @param value    值
     * @return 相似条件
     */
    public static Condition like(String property, String alias, Object value) {
        return new Condition(property, alias, Operator.like, value);
    }

    public static Condition like(String property, Object value) {
        return new Condition(property, "", Operator.like, value);
    }

    /**
     * 返回包含条件
     *
     * @param property 属性
     * @param alias    别名
     * @param value    值
     * @return 包含条件
     */
    public static Condition in(String property, String alias, Collection<Object> value) {
        return new Condition(property, alias, Operator.in, Lists.newArrayList(value));
    }

    public static Condition in(String property, Object... value) {
        if (value.length == 1 && Collection.class.isAssignableFrom(value[0].getClass())) {
            return new Condition(property, "", Operator.in, Lists.newArrayList((Collection) value[0]));
        }
        return new Condition(property, "", Operator.in, Lists.newArrayList(value));
    }

    /**
     * 返回为Null条件
     *
     * @param property 属性
     * @param alias    别名
     * @return 为Null条件
     */
    public static Condition isNull(String property, String alias) {
        return new Condition(property, alias, Operator.isNull, null);
    }

    public static Condition isNull(String property) {
        return new Condition(property, "", Operator.isNull, null);
    }

    /**
     * 返回不为Null条件
     *
     * @param property 属性
     * @param alias    别名
     * @return 不为Null条件
     */
    public static Condition isNotNull(String property, String alias) {
        return new Condition(property, alias, Operator.isNotNull, null);
    }

    public static Condition isNotNull(String property) {
        return new Condition(property, "", Operator.isNotNull, null);
    }

    /**
     * 获取属性
     *
     * @return 属性
     */
    public String getProperty() {
        return property;
    }

    /**
     * 获取别名
     *
     * @return 别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 获取运算符
     *
     * @return 运算符
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * 获取值
     *
     * @return 值
     */
    public Object getValue() {
        return value;
    }

    public List<Condition> getOrList() {
        return orList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Condition condition = (Condition) o;

        if (!property.equals(condition.property)) return false;
        if (!alias.equals(condition.alias)) return false;
        if (operator != condition.operator) return false;
        if (!value.equals(condition.value)) return false;
        return orList.equals(condition.orList);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int result = property.hashCode();
        result = 31 * result + alias.hashCode();
        result = 31 * result + operator.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + orList.hashCode();
        return result;
    }
}
