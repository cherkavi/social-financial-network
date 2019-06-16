package bonpay.partner.web_gui.common;

import java.io.Serializable;
import java.util.HashMap;

/** объект, который контроллирует возможность получения ответов от страницы, 
 * и перехода на следующие страницы */
public class RedirectAction implements Serializable{
	private final static long serialVersionUID=1L;
	private HashMap<String, RedirectActionPage> map=new HashMap<String,RedirectActionPage>();
	/** объкт, который содержит в себе необходимую информацию по передаче управления следующим страницам 
	 * @param returnValues (not null)- значения, для которых регистрируется передача управления   
	 * @param pages (not null)- страницы, на которые следует передавать управление 
	 * */
	public RedirectAction(String[] returnValues,RedirectActionPage[] pages){
		for(int counter=0;counter<returnValues.length;counter++){
			map.put(returnValues[counter], pages[counter]);
		}
	}
	
	/** перевести управление от указанного компонента на страницу 
	 * @param component - компонент, которому будет делегировано право передачи управления (component.setResponsePage)
	 * @param returnValue - текстовое значение для идентификации передачи управления 
	 * @param methodName - (nullable)имя метода, которому будет передано управление
	 * @param executeClass -  (nullable)(executeClass.length==executeObject.length) классы параметров 
	 * @param executeObject - (nullable)(executeClass.length==executeObject.length) параметры
	 * <table border=1 style="background:gray">
	 * 	<tr>
	 * 		<th>Ветка</th> <th>Имя метода</th> <th>Параметры</th> <th> Описание </th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPage</td> <td>null</td>   <td>any</td>         <td> параметры и/или метод не используются</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPage</td> <td>value</td> <td>any</td>          <td> параметры и/или метод не используются</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>null</td> <td>null</td>  <td> передача управления странице</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>null</td> <td>value</td> <td> параметры и/или метод не используются</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>value</td> <td>null</td> <td> вызов метода у страницы перед передачей управления</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>value</td> <td>value</td> <td> вызов метода c параметрами у страницы перед передачей управления</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>null</td> <td>null</td>  <td> передача управления созданному объекту из класса</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>null</td> <td>value</td> <td> добавление к конструктору страницы указанных параметров </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>value</td> <td>null</td> <td> создание страницы, затем вызов метода у этой страницы</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>value</td> <td>value</td> <td> создание страницы, затем вызов метода c параметрами у этой страницы</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>null</td> <td>null</td>  <td> передача управления созданному классу</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>null</td> <td>value</td> <td> добавление к методу получения страницы указанных параметров </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>value</td> <td>null</td> <td> получение страницы из объекта, затем вызов метода у этой страницы</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>value</td> <td>value</td> <td> получение страницы из объекта, затем вызов метода у этой страницы c параметрами</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>null</td> <td>null</td>  <td> вызываем метод у объекта, затем передаем управление странице</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>null</td> <td>value</td> <td> вызываем метод у объекта с добавленными параметрами, затем перeдаем управление странице</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>value</td> <td>null</td> <td> вызываем метод у объекта, затем у страницы так же вызываем метод</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>value</td> <td>value</td> <td> вызываем метод у объекта, затем у страницы так же вызываем метод c параметрами</td>
	 * 	</tr>
	 * </table> 
	 * */
	public boolean action(org.apache.wicket.Component component,
						  String returnValue,
						  String methodName,
						  Class<?>[] executeClass,
						  Object[] executeObject){
		boolean value=false;
		if(map.containsKey(returnValue)){
			value=map.get(returnValue).action(component,methodName, executeClass, executeObject);
		}else{
			System.err.println("no ActionPage for returnValue:"+returnValue);
			value=false;
		}
		return value;
	}
}
