package bonclub.reports.web_service.interf;

import bonclub.reports.web_service.common.RemoteReportFileDescription;

/** интерфейс для предоставления установки отчетов в очередь на изготовление */
public interface IReporter {
	/** добавить отчет в очередь  
	 * @param byteArray - сериализованные объекты в виде потока байт
	 * @return возвращаемое значение: 
	 * <ul>
	 * 	<li><b>true</b> - задача успешно установлена </li>
	 * 	<li><b>false</b> - ошибка установки задачи </li>
	 * </ul>
	 * */
	public boolean addReportForProcess(byte[] byteArray);

	/** на основании номера отчета из списка задач получить ссылку на файл, 
	 * который
	 * @param userId - уникальный номер пользователя, для которого данный отчет создан 
	 * @param idReportTask - уникальный номер отчета, ссылку на который нужно получить   
	 * на основании которого строятся данные
	 * @return ссылка на данный отчет 
	 * */
	public RemoteReportFileDescription getReportUrl(Integer userId, Integer reportId);
}
