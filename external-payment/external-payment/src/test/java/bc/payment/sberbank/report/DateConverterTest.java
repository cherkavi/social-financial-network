package bc.payment.sberbank.report;

import java.util.Date;

import org.junit.Test;
import org.simpleframework.xml.stream.Mode;
import org.simpleframework.xml.stream.NamespaceMap;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import bc.payment.sberbank.report.parser.DateTimeConverter;
import junit.framework.Assert;

public class DateConverterTest {

	@Test
	public void checkDateConversionTest() throws Exception{
		// given
		@SuppressWarnings("deprecation")
		Date date=new Date(2015-1900,11,21,19,33,54);
		DateTimeConverter converter=new DateTimeConverter();
		MockOutputNode node=new MockOutputNode();

		// when
		converter.write(node, date);

		// then
		Assert.assertEquals("2015-12-21 19:33:54", node.getValue());
	}
}

class MockOutputNode implements OutputNode{

	@Override
	public String getName() {
		return "mock";
	}

	@Override
	public void setName(String arg0) {
	}

	private String value=null;
	@Override
	public void setValue(String value) {
		this.value=value;
	}

	@Override
	public String getValue() throws Exception {
		return this.value;
	}

	@Override
	public void commit() throws Exception {
	}

	@Override
	public NodeMap<OutputNode> getAttributes() {
		return null;
	}

	@Override
	public OutputNode getChild(String arg0) throws Exception {
		return null;
	}

	@Override
	public String getComment() {
		return null;
	}

	@Override
	public Mode getMode() {
		return null;
	}

	@Override
	public NamespaceMap getNamespaces() {
		return null;
	}

	@Override
	public OutputNode getParent() {
		return null;
	}

	@Override
	public String getPrefix() {
		return null;
	}

	@Override
	public String getPrefix(boolean arg0) {
		return null;
	}

	@Override
	public String getReference() {
		return null;
	}

	@Override
	public boolean isCommitted() {
		return true;
	}

	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public void remove() throws Exception {
	}

	@Override
	public OutputNode setAttribute(String arg0, String arg1) {
		return this;
	}

	@Override
	public void setComment(String arg0) {
	}

	@Override
	public void setData(boolean arg0) {
	}

	@Override
	public void setMode(Mode arg0) {
	}

	@Override
	public void setReference(String arg0) {
	}

}
