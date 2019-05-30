package bc.ws.domain;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlElement;

/**
 *  
 */
@XmlRootElement(name = "terminalMenuResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalMenuResult extends CommonResult{
	// a.id_menu_element, -- ИД операции в БД
	private Integer idMenu;
	// a.name_menu_element, -- Название операции
    @XmlElement(name = "nameMenu")
	private String name;
	// a.title_menu_element, -- Текст названия операции, который будет выводиться в форму
    @XmlElement(name = "titleMenu")
	private String title;
	// a.tabname_menu_element, -- Название закладки - НЕ МЕНЯЕТСЯ, по этому полю будет осуществляться проверка доступа к функции
    @XmlElement(name = "codeMenu")
	private String tabName;
	// a.id_menu_element_parent, -- ИД родительской операции
	@XmlElement(name = "idMenuParent")
	private Integer idParent;
	// a.order_number, -- Порядок вывода в форме
    @XmlElement(name = "orderNumber")
	private Integer idOrder;
	// a.relative_path, -- Путь к вызываемому файлу
	// private String path;
	// a.exec_file, -- Вызываемый файл
	// private String execFile;
	// a.id_privilege_type, -- ИД уровня доступа (0 - доступ закрыт, 1 - только для чтения, 2 и 9 - выполнение
	private AccessType accessType;
	// a.name_privilege_type, -- Название уровня доступа
	private String accessName;
	// a.img_src, -- Ссылка на картинку для операции
	// private String imageSrc;
	// a.has_help, -- ‘Y’ - есть справка по операции
	// private Boolean help;
	// a.is_enable, -- ‘Y’ - операция доступна, ‘N’ - операция недоступна (высвечивается на экран, но выполнить ее нельзя)
	private Boolean enabled;
	// a.is_visible -- ‘Y’ - операция видима пользователю, ‘N’ - операция е выводится в форму
	// private Boolean visible;
	
	
	public Integer getIdMenu() {
		return idMenu;
	}
	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public Integer getIdParent() {
		return idParent;
	}
	public void setIdParent(Integer idParent) {
		this.idParent = idParent;
	}
	public Integer getIdOrder() {
		return idOrder;
	}
	public void setIdOrder(Integer idOrder) {
		this.idOrder = idOrder;
	}
	public AccessType getAccessType() {
		return accessType;
	}
	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}
	public String getAccessName() {
		return accessName;
	}
	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

    @Override
    public String toString() {
        return "TerminalMenuResult{" +
                "idMenu=" + idMenu +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", tabName='" + tabName + '\'' +
                ", idParent=" + idParent +
                ", idOrder=" + idOrder +
                ", accessType=" + accessType +
                ", accessName='" + accessName + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
