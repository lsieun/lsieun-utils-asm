/**
 * 这个package的主要作用就是进行“查找”，它还能将“查找的结果”封装成Result对象进行返回。
 * <br/><br/>
 * <p>在具体代码实现上，将这个package分成了两类：</p>
 * <ul>
 *     <li>第一类：clazz，不需要查看方法内的opcode，比如查找父类、接口、方法名、字段名</li>
 *     <li>第二类：opcode，需要查看方法内的opcode</li>
 * </ul>
 * <p>而第二类opcode又分成两种：</p>
 * <ul>
 *     <li>第一种：ref，表示“这个方法”在什么地方进行了调用（被动）</li>
 *     <li>第二种：invoke，表示“这个方法”都调用了什么方法（主动）</li>
 * </ul>
 */
package lsieun.asm.adapter.find;