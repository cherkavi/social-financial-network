package utility.model;

/** интерфейс для получения данных  */
public interface IModel<T> {
	
	/** получить значение модели*/
	public T getModelValue();
	
	/** установить значение модели */
	public void setModelValue(T value);
	
}
