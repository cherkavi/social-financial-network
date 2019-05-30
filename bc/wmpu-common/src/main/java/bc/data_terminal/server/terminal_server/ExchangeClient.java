package bc.data_terminal.server.terminal_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import BonCard.DataBase.UtilityConnector;
import bc.data_terminal.server.csv.WrapXmlChecker;
import bc.data_terminal.server.database.ClientIdentifier;
import bc.data_terminal.server.database.Connector;
import bc.data_terminal.server.database.CreatedFile;
import bc.data_terminal.server.database.DBFunction;
import bc.data_terminal.server.database.DbField;
import bc.data_terminal.server.terminal.transport.Data;
import bc.data_terminal.server.terminal.transport.Task;
import bc.data_terminal.server.terminal.transport.Transport;

import com.csvreader.CsvReader;

/** �����, ������� ������������ ������� �� �������(Transport) � ������ �� ��� ������ (Transport)*/
public class ExchangeClient {
	private final static Logger LOGGER=Logger.getLogger(ExchangeClient.class);
	/** INFO ����� ����� ���-�� ������� ���������� ������ COMMIT � ��������� */
	private final static int commitRecordValue=1000;
	private String tempDirectory="";
	private static SimpleDateFormat sdf=new SimpleDateFormat("yy_MM_dd__HH_mm_ss");
	
	public byte[] convertDocumentToBytes(Document document){
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try{
			javax.xml.transform.TransformerFactory transformer_factory = javax.xml.transform.TransformerFactory.newInstance();  
			javax.xml.transform.Transformer transformer = transformer_factory.newTransformer();  
			javax.xml.transform.dom.DOMSource dom_source = new javax.xml.transform.dom.DOMSource(document);   
			//string_writer = new Packages.java.io.StringWriter();  
			javax.xml.transform.stream.StreamResult stream_result = new javax.xml.transform.stream.StreamResult(baos);  
			transformer.transform(dom_source, stream_result);
		}catch(Exception ex){
			LOGGER.debug("convertDocumentToBytes: Exception:"+ex.getMessage());
		}
		return baos.toByteArray();
	}
	
	
    /** �������������� ��������� ���� � ����� ����
     * @param path_to_file ���� � �����
     * @return ���������� byte[] 
     */
    public byte[] getZipBytesFromFile(String path_to_file){
        byte[] return_value=null;
        try{
            File file_source=new File(path_to_file);
            if(file_source.exists()){
                BufferedInputStream origin = null;
                ByteArrayOutputStream dest=new ByteArrayOutputStream();
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                //out.setMethod(ZipOutputStream.DEFLATED);
                byte data[] = new byte[2048];
                FileInputStream file = new FileInputStream(path_to_file);
                origin = new BufferedInputStream(file, 2048);
                // �������� � Zip ����� ��� �����, ��� ��� ������ ���� 
                ZipEntry entry = new ZipEntry(file_source.getName());
                out.putNextEntry(entry);
                int count;
                while((count = origin.read(data, 0, 2048)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
                out.close();
                dest.close();
                return_value=dest.toByteArray();
            }else{
                LOGGER.debug("fileToArchiv: file not found");
            }
        }catch(Exception ex){
            LOGGER.error("getZipBytesFromFile:"+ex.getMessage());
        }
        return return_value;
    }
    
    /** �������� ���������������� ������(� ������ ����� )  �� ��������� byte[] 
     * @param zip_file_name ��� ZIP ����� ��� ������� ����� ������ ������ � ������
     * @param data ������ ��� ������������� 
     * */
    public byte[] getZipBytesFromBytes(String zip_file_name,byte[] data){
        byte[] return_value=null;
        try{
            ByteArrayOutputStream dest=new ByteArrayOutputStream();
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            //out.setMethod(ZipOutputStream.DEFLATED);
            BufferedInputStream origin = new BufferedInputStream(new ByteArrayInputStream(data), 2048);
            byte[] temp_data=new byte[2048]; 
            // �������� � Zip ����� ��� �����, ��� ��� ������ ����
            ZipEntry entry = new ZipEntry(zip_file_name);
            out.putNextEntry(entry);
            int count;
            while((count = origin.read(temp_data, 0, 2048)) != -1) {
                out.write(temp_data, 0, count);
            }
            origin.close();
            out.close();
            dest.close();
            return_value=dest.toByteArray();
        }catch(Exception ex){
            LOGGER.error("getZipBytesFromFile:"+ex.getMessage());
        }
        return return_value;
    }
    
    /** ��������������� ����� ���� � ���� Zip ������ � ���� �� ����� 
     * @param data ������ ���� ������� Zip
     * @param path_to_file ��� ����� ��� ���������� �� ������
     * @return true ���� ���� ������� ��������
     */
    public boolean getFileFromZipBytes(byte[] data,String path_to_file){
        boolean return_value=false;
        try{
            // �������� ����� 
            ByteArrayInputStream fis=new ByteArrayInputStream(data);
            // �������� zip �����
            ZipInputStream zis=new ZipInputStream(new BufferedInputStream(fis));
            // ��������� ��������� ��������
            ZipEntry entry;
            BufferedOutputStream dest;
            while((entry=zis.getNextEntry())!=null){
                if(entry.isDirectory()){
                    LOGGER.debug("this is directory:"+entry.getName());
                }else{
                    LOGGER.debug("this is file:"+entry.getName());
                    int count;
                    byte buffer[] = new byte[2048];
                    // write the files to the disk
                    FileOutputStream fos = new FileOutputStream(path_to_file);
                    dest = new BufferedOutputStream(fos, 2048);
                    while ((count = zis.read(buffer, 0, 2048)) != -1) {
                       dest.write(buffer, 0, count);
                    }
                    dest.flush();
                    dest.close();
                }
            }
            zis.close();
            return_value=true;
        }catch(Exception ex){
            LOGGER.error("getFileFromZipBytes Exception:"+ex.getMessage());
        }
        return return_value;
    }
	
    //private String pathToTempDirectory;
	/** ������, ������� ������������ �������� ���������� ������� � �������� �� ��� 
	 * @param tempDirectory ��������� ������� � ������� �������� ��������� ������ 
	 * */
	public ExchangeClient(String tempDirectory) throws Exception{
		this.tempDirectory=tempDirectory;
		LOGGER.debug(" constructor done: "+this.tempDirectory);
		/*File file=new File(tempDirectory);
		if((file.isDirectory()==false)||(file.exists()==false)){
			throw new Exception(tempDirectory+" it is not Directory");
		}else{
			//this.pathToTempDirectory=tempDirectory;
		}*/
	}
	
	/** �������� ���������� ������ � �������� �� ���� 
	 * @request - ������, ������� ������� ������
	 * @return - �����, ������� ������ �������� �� ��������� ���������� ������ 
	 * */
	public Transport decode(Transport request){
		LOGGER.debug("decode:begin");
		if((request!=null)&&(request.getTask()!=null)){
			LOGGER.debug("ExchangeClient: "+request.getTask().getName());
			// INFO Server.Function PARSING
			if(request.getTask().getName().equals("CLIENT_DOWNLOAD_HISTORY")){
				Task task=request.getTask();
				/*Task task=new Task(functionName);
				task.addData(new Data(taskId));
				task.addData(new Data(this.dateBegin));
				task.addData(new Data(this.dateEnd));*/
				LOGGER.debug("TaskId:"+task.getData(0).getObject());
				LOGGER.debug("Date begin:"+task.getData(1).getObject());
				LOGGER.debug("Date end:"+task.getData(2).getObject());

				Connection connection=null;
				try{
					connection=Connector.getConnection();
					DBFunction function=new DBFunction();
					ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"),function,connection);
					Serializable[][] objects=function.getHistory(identifier.getTerminalId(),
																   (String)task.getData(0).getObject(),
																   (Date)task.getData(1).getObject(), 
																   (Date)task.getData(2).getObject(),
																   "DOWNLOADED",
																   connection
																   );
					
					request.getTask().clearData();
					request.getTask().addData(new Data(objects));
					request.getTask().setStateIsDone();
					request.setDirectionFromServer();
				}finally{
					UtilityConnector.closeQuietly(connection);
				}
			}
			
			// INFO: Server: CLIENT_REQUEST_HISTORY
			if(request.getTask().getName().equals("CLIENT_UPLOAD_HISTORY")){
				Task task=request.getTask();
				/*Task task=new Task(functionName);
				task.addData(new Data(taskId));
				task.addData(new Data(this.dateBegin));
				task.addData(new Data(this.dateEnd));*/
				LOGGER.debug("TaskId:"+task.getData(0).getObject());
				LOGGER.debug("Date begin:"+task.getData(1).getObject());
				LOGGER.debug("Date end:"+task.getData(2).getObject());
				Connection connection=null;
				try{
					connection=Connector.getConnection();
					DBFunction function=new DBFunction();
	
					ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"),function,connection);
					
					Serializable[][] objects=function.getHistory(identifier.getTerminalId(),
																   (String)task.getData(0).getObject(),
																   (Date)task.getData(1).getObject(), 
																   (Date)task.getData(2).getObject(),
																   "UPLOADED",
																   connection
																   );
					
					request.getTask().clearData();
					request.getTask().addData(new Data(objects));
					request.getTask().setStateIsDone();
					request.setDirectionFromServer();
				}finally{
					UtilityConnector.closeQuietly(connection);
				}
			}
			
			// INFO: CLIENT_REQUEST_LOAD_FILE_CATALOG_GET
			if(request.getTask().getName().equals("CLIENT_REQUEST_LOAD_FILE_CATALOG_GET")){
				Integer taskId=Integer.parseInt((String)(request.getTask().getData().getObject()));

				Connection connection=null;
				try{
					connection=Connector.getConnection();
				DBFunction function=new DBFunction();
				ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"),function,connection);
				
				String serverResponse=function.getStringFromDataBase(identifier, 
							  										   taskId,
																	   "CLIENT_LOAD_FILE_CATALOG",
																	   connection
							  										   );
				if(serverResponse==null){
					serverResponse="";
				}
				request.getTask().clearData();
				request.getTask().addData(new Data(serverResponse));
				request.getTask().setStateIsDone();
				request.setDirectionFromServer();
				}finally{
					UtilityConnector.closeQuietly(connection);
				}
			}
			// INFO: CLIENT_REQUEST_LOAD_FILE_CATALOG_SET ��������� �������� ��� �������� ������ ������� 
			if(request.getTask().getName().equals("CLIENT_REQUEST_LOAD_FILE_CATALOG_SET")){
				Integer taskId=null;
				String value="";
				try{
					taskId=Integer.parseInt((String)(request.getTask().getData().getObject()));
				}catch(Exception ex){};
				try{
					value=(String)(request.getTask().getData(1).getObject());
				}catch(Exception ex){};
				
				Connection connection=null;
				try{
					connection=Connector.getConnection();
					DBFunction function=new DBFunction();
					ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"),function,connection);
					if (function.putObjectToDataBase(identifier,
													   taskId,
													   "CLIENT_LOAD_FILE_CATALOG", 
													   value,
													   connection)) {
						request.getTask().clearData();
						request.getTask().setStateIsDone();
					} else {
						request.getTask().clearData();
						request.getTask().setStateIsError();
					}
					request.setDirectionFromServer();
				}finally{
					UtilityConnector.closeQuietly(connection);
				}
			}
			
			
			// INFO: Server: CLIENT_RESPONSE_FILE
			if(request.getTask().getName().equals("CLIENT_RESPONSE_FILE")){
				LOGGER.debug(request.getTask().getName());
				String clientFileName=request.getTask().getData().getDataName();
				Object objectForSave=request.getTask().getData().getObject();
				String taskId=(String)request.getTask().getData(1).getObject();
				boolean saveResult=false;
				Connection connection=null;
				try{
					connection=Connector.getConnection();
					DBFunction function=new DBFunction();
					ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"),function,connection);
					String errorInformation=null;
					if(taskId==null){
						LOGGER.error("TaskId is not recognized ");
						/*saveResult=
							DBFunction.putObjectToDataBase(identifier,
									   					   0,
									   					   "CLIENT_RESPONSE_FILE", 
									   					   objectForSave);*/
					}else{
						// TODO CLIENT_RESPONSE_FILE put file into DataBase
						if(objectForSave instanceof byte[]){
							//getResultForSaveFileIntoDatbase
							//saveResult=DBFunction.putObjectToDataBase(identifier,taskId,"CLIENT_RESPONSE_FILE", objectForSave);
							// ��������� ���� �� ��������� ��������� 
							String uniqueFileName=tempDirectory+sdf.format(new Date())+"_"+identifier.getTerminalId()+"_"+taskId+".csv";
							LOGGER.debug("FileForSave: "+uniqueFileName);
							if(this.getFileFromZipBytes((byte[])objectForSave, uniqueFileName)){
								// ���� ������� �������� - ������� � ���� ������
	
								if(getResultForSaveFileIntoDatbase(connection,function, identifier, clientFileName,uniqueFileName,taskId)){
									saveResult=true;
								}else{
									errorInformation=": �� �������� � ���� ������";
									saveResult=false;
								}
								
							}else{
								// ���� �� ��������
								saveResult=false;
								errorInformation=": �� ����������";
							}
						}
					}
					if(saveResult==true){
						request.getTask().clearData();
						request.getTask().setStateIsDone();
						request.getTask().setInformation(": �������� ");
					}else{
						request.getTask().clearData();
						request.getTask().setStateIsError();
						request.getTask().setInformation(errorInformation);
					}
					request.setDirectionFromServer();
				}finally{
					UtilityConnector.closeQuietly(connection);
				}
			};
			
			
			// INFO: Server: Stub CLIENT_REQUEST_LOAD_FILE - ����������� ������ �������� ������ �� ������  
			if(request.getTask().getName().equals("CLIENT_REQUEST_LOAD_FILE")){
				LOGGER.debug(request.getTask().getName());
				Connection connection=null;
				try{
					connection=Connector.getConnection();
					DBFunction function=new DBFunction();
					ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"),function,connection);
					ArrayList<CreatedFile> files=function.getFileForSendToClient(tempDirectory, identifier, (String)request.getTask().getData().getObject(),connection);
					request.getTask().clearData();
					for(int counter=0;counter<files.size();counter++){
						request.getTask().addData(new Data(files.get(counter).getRemoteFileName(),this.getZipBytesFromFile(files.get(counter).getThisFileName())));
					}
					request.getTask().setStateIsDone();
					request.setDirectionFromServer();
					if(function.setFileState(files, "UPLOADED",connection)!=files.size()){
						LOGGER.error("CLIENT_REQUEST_LOAD_FILE: change state Error ");
					}
				}finally{
					UtilityConnector.closeQuietly(connection);
				}
			}
			
			// INFO: GET_XML_CHECKER (Data[0] consists Name of Task)
			if(request.getTask().getName().equals("GET_XML_CHECKER")){
				LOGGER.debug(request.getTask().getName());
				LOGGER.debug("set Client Request File format for operation:"+request.getTask().getData().getObject());
				String frameName=null;
				String taskId=null;
				try{
					frameName=(String)request.getTask().getData().getObject();
				}catch(Exception ex){};
				try{
					taskId=(String)request.getTask().getData(1).getObject();
				}catch(Exception ex){};
				String format_file=null;
				Connection connection=null;
				try{
					connection=Connector.getConnection();
					DBFunction function=new DBFunction();
					ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"),function,connection);
					
					if(taskId==null){
						// �� ������������� ���� - ����������� �������� ������  
						format_file=function.getXmlFormatFile(identifier, 
			                        					      frameName,
			                        						  connection
																);
					}else{
						// ������������� ����
						format_file=function.getXmlFormatFile(identifier, 
															  taskId,
															  connection
															    );
					}
					if(format_file!=null){
						request.getTask().clearData();
						request.getTask().addData(new Data("pattern.xml",this.getZipBytesFromBytes("pattern.xml", format_file.getBytes())));
						request.getTask().setStateIsDone();
					}else{
						request.getTask().clearData();
						request.getTask().setStateIsError();
					}
					request.setDirectionFromServer();
				}finally{
					UtilityConnector.closeQuietly(connection);
				}
			}
			// INFO: Server: GET_XML_VISUAL ��������� ����� ���������� ����������� ���������� �����������
			if(request.getTask().getName().equals("GET_XML_VISUAL")){
				LOGGER.debug(request.getTask().getName());
				if(request.getTask().getDataCount()==0){
					// Server: GET_XML_FILE �������� ����������� ������ � ������
					// Server: ��������� ������, ���� ������ ������������
					LOGGER.debug("login:"+request.getTransportParameter("login"));
					LOGGER.debug("password:"+request.getTransportParameter("password"));
					// ������ ����� �������
					request.getTask().clearData();
					// ��� ������������ �� ����
					Connection connection=null;
					try{
						connection=Connector.getConnection();
						DBFunction function=new DBFunction();
						ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"),function,connection);
						Document user_document=function.getVisualXmlByUser(identifier,connection);
						// delete tracing 
						/*BufferedReader br=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.convertDocumentToBytes(user_document))));
				        String temp_string;
				        try{
					        while( (temp_string=br.readLine())!=null){
					        	LOGGER.LOGGER.debug("server send>>> "+temp_string);
					        }
				        }catch(Exception ex){}
				        */
						request.getTask().addData(new Data("for_access.xml",
															this.getZipBytesFromBytes("for_access.xml",
																					  this.convertDocumentToBytes(user_document)
														                              )
								                  		   )
						                          );
						// Data not valid
						//request.getTask().addData(new Data("for_access.xml",null));
						request.getTask().setStateIsDone();
						request.setDirectionFromServer();
					}finally{
						UtilityConnector.closeQuietly(connection);
					}
				}else{
					LOGGER.error("GET_XML_VISUAL error TASK inner structure ");
				}
			}
			// INFO: GET_STRING_FROM_SERVER ( key=PROGRAM_TITILE ) 
			if(request.getTask().getName().equals("GET_STRING_FROM_SERVER")){
				LOGGER.debug(request.getTask().getName());
				String key=(String)request.getTask().getData().getObject();
				if(key.equals("PROGRAM_TITLE")){
					Connection connection=null;
					try{
						connection=Connector.getConnection();
						DBFunction function=new DBFunction();
						ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"),function,connection);
						String caption=function.fillIdentifier(identifier,connection);
						if((caption!=null)&&(!caption.equals(""))){
							request.getTask().clearData();
							request.getTask().addData(new Data(caption));
						}else{
							request.getTask().clearData();
							request.getTask().addData(new Data("��������"));
						}
					}finally{
						UtilityConnector.closeQuietly(connection);
					}
				}
			}
		}else{
			// request is null
			if(request==null){
				LOGGER.error("client request is null ");
			}else{
			// request.getTask is null
				if(request.getTask()==null){
					LOGGER.error("client Transport not Exists Task");
				}else{
					LOGGER.error("client unknown ERROR");
				}
			}
			request=null;
		}
		LOGGER.debug("decode:end");
		return request;
	}
	
	/** ��������� ������ �� ���� � ���� �����  
	private boolean saveByteArrayIntoFile(byte[] array, String fileName){
		boolean returnValue=false;
		try{
			FileOutputStream output=new FileOutputStream(fileName);
			ByteArrayInputStream input=new ByteArrayInputStream(array);
			byte[] buffer=new byte[1024];
			int count=0;
			while((count=input.read(buffer))>=0){
				output.write(buffer,0,count);
				output.flush();
			}
			output.close();
			returnValue=true;
		}catch(Exception ex){
			LOGGER.error("ExchangeClient#saveByteArrayIntoFile "+ex.getMessage());
		}
		return returnValue;
	}
*/
	
	/** �������� �� ������� ���� � ����� ��� ����� */
/*	private String getFileName(String filePath){
		String returnValue=new String(filePath);
		int lastIndex=returnValue.lastIndexOf(fileSeparator);
		if(lastIndex>=0){
			returnValue=filePath.substring(lastIndex+fileSeparator.length());
		}
		return returnValue;
	}
*/	
	
	/** ����� ��� ������������ ������ ���������,<br> 
	 * � ������ ������ ���������� ������������ �����, ������� �������� ��  
	 */
/*	public static void main(String[] args){
		LOGGER.LOGGER.debug("begin:");
		try{
			ExchangeClient exchange=new ExchangeClient();
			ClientIdentifier identifier=new ClientIdentifier("login","password");
			DBFunction.fillIdentifier(identifier);
			boolean answer=exchange.getResultForSaveFileIntoDatbase(identifier, // ���������� ������������� ������� 
													 "temp_check.csv", // ��� �����, ������� ������� ������
													 "d:\\check.csv", // ������ ���� � �����, ������� �������� �� ����� ��� ������ ������
													 "FrameOperationPutFile");// ������ ������������ ����������� �����������
			LOGGER.LOGGER.debug("answer:"+answer);
			// �������� ������� ������: 
		}catch(Exception ex){
			LOGGER.error("ExchangeClient:"+ex.getMessage());
		}
		LOGGER.LOGGER.debug("end:");
	}
*/	
	/** ���������� ��������� ���������� ���� � ���� ������ 
	 * @param indentifier - ���������� ������������� �������
	 * @param fileNameClient - ��� �����, ������� ������� ������
	 * @param pathToFile - ������ ���� � ����� 
	 * @param taskId - ���������� ����� ������ � �������� ������� 
	 * */
	private boolean getResultForSaveFileIntoDatbase(Connection connection,
													DBFunction function,
													ClientIdentifier identifier,
												    String fileNameClient, 
												    String pathToFile,
												    String taskId){
		boolean returnValue=false;
		// �������� ������� � ���� ������, ������� ����������������� ���
		try{
			LOGGER.debug("getResultForSaveFileIntoDatbase: terminal_id:"+identifier.getTerminalId());
			LOGGER.debug("getResultForSaveFileIntoDatbase: taskId:"+taskId);
			// �������� ����� �������
			String idExchangeFile=function.getFileIdByTaskName(taskId,connection);
			LOGGER.debug("getResultForSaveFileIntoDatbase: patternFileId:"+idExchangeFile);
			Integer idFileTransfer=function.getIdFileTransfer(connection);
			// �������� ���������� ����� �������
			Integer fileIdentifier=function.getIdOfSend(identifier.getTerminalId(),
					   taskId,
					   idFileTransfer,
					   fileNameClient, // client file name
					   pathToFile, // path to file
					   new Date(), // date of save
					   "DOWNLOADED", // status
					   "",// text of error
					   Integer.valueOf(idExchangeFile), // pattern identifier  
					   null,//Club id  - null as default
					   connection); 
			LOGGER.debug("getResultForSaveFileIntoDatbase: fileIdentifier:"+fileIdentifier);
			// �������� ��� �������, � ������� ����� ���������� ���� // SELECT db_table_name FROM BC_ADMIN.v_dt_exchange_file WHERE id_exchange_file = <patternFileId>
			String tableNameForSave=function.getTableNameForSaveDataFromFile(idExchangeFile,connection);
			LOGGER.debug("getResultForSaveFileIntoDatbase: TableName for save Data:"+tableNameForSave);
			// �������� ����, ������� ���������� � �������
			ArrayList<DbField> listOfField=function.getTableFieldsForSaveDataFromFile(idExchangeFile,connection);
			LOGGER.debug("getResultForSaveFileIntoDatbase: ����� ���-�� ����� � ���� ������: "+listOfField.size());
			// ������� �����-������� ��� ������������ XML �����
			WrapXmlChecker xmlChecker=new WrapXmlChecker(function.getXmlFormatFile(identifier, taskId,connection));
			// INFO: ������ ��� ������ �� ����� CSV � ���� ������� ����
			returnValue=saveFromCsvToDataBase(pathToFile,
											  tableNameForSave,
											  listOfField,
											  fileIdentifier,
											  xmlChecker);
		}catch(Exception ex){
			LOGGER.error("getResultForSaveFileIntoDatbase Exception:"+ex.getMessage());
		}
		return returnValue;
	}
	
	/** ��������� ������ �� CSV ����� � ���� ������ 
	 * @param pathToCsv - ������ ���� � ����� CSV
	 * @param tableName - ��� ������� � ���� ������ ��� ���������� ������ 
	 * @param list - ������ �� �����, ������� ����� ������ �� CSV � ������ � ���� ������
	 * @param fileId - ���������� ��� ������ ����� ( IdFileTransfer )s
	 * @return true - ���� ������ ������ �������, 
	 */
	private boolean saveFromCsvToDataBase(String pathToCsv, 
										  String tableName, 
										  ArrayList<DbField> list,
										  Integer fileId,
										  WrapXmlChecker xmlWrap){
		boolean returnValue=false;
		Connection connection=null;
		CsvReader reader=null;
		/** ��� �� ���������� Commit � �������� ������ ������ � ����  */
		boolean commitIsSet=false;
		try{
			// ������� ������ � ���� ������ 
			connection=Connector.getConnection();
			connection.setAutoCommit(false);
			StringBuilder buffer=new StringBuilder();
			buffer.append("INSERT INTO "+tableName+"(ID_FILE_TRANSFER");
			for(int counter=0;counter<list.size();counter++){
				buffer.append(",");
				buffer.append(list.get(counter).getFieldName());
			}
			buffer.append(") VALUES(?");
			for(int counter=0;counter<list.size();counter++){
				buffer.append(",");
				buffer.append("?");
			}
			buffer.append(")");
			LOGGER.debug(buffer.toString());
			PreparedStatement statement=connection.prepareStatement(buffer.toString());
			// ��������� ��� ���� �� ���� ������ � ���������� �� �
			reader=new CsvReader(new InputStreamReader(new FileInputStream(new File(pathToCsv))));
			// ��������� CSV ���� �� ������� � ��� ����������
			if(xmlWrap.isHeaderExists()){
				reader.readHeaders();
			}
			/** ����������, ������� ������������ ���-�� ���������� � ���� ������ */
			int recordCounter=0;
			while(reader.readRecord()){
				// �������� ��������� ������
				statement.clearParameters();
				statement.setInt(1, fileId);
				// INFO: server ����� ���������� ����� �� ����� - ���������� ������� INSERT
				for(int counter=0;counter<list.size();counter++){
					try{
						Object writeElement=convertStringToObject(reader.get(counter),list.get(counter).getFieldType());
						LOGGER.debug((counter+2)+":"+writeElement);
						LOGGER.debug("   Object:"+writeElement.getClass().getName()); 
						statement.setObject(counter+2, convertStringToObject(reader.get(counter),list.get(counter).getFieldType()));
					}catch(Exception ex){
						statement.setNull(counter+2, list.get(counter).getFieldType());
						LOGGER.error("read record Exception:"+ex.getMessage());
					}
				}
				statement.executeUpdate();
				recordCounter++;
				if((recordCounter%ExchangeClient.commitRecordValue)==0){
					// commit as needed
					commitIsSet=true;
					connection.commit();
				}
			}
			reader.close();
			connection.commit();
			returnValue=true;
		}catch(Exception ex){
			try{
				connection.rollback(); // �������� ��� ���������
				if(commitIsSet){
					// ������� ��� ������ �� ������� �� �����
					try{
						connection.createStatement().executeUpdate("DELETE FROM "+tableName+" WHERE ID_FILE_TRANSFER="+fileId);
						connection.commit();
					}catch(Exception ex2){
						LOGGER.error("error delete for ID_FILE_TRANSFER="+fileId+"\n Exception:"+ex2.getMessage());
					}
				}
			}catch(Exception exInner){};
			LOGGER.error("saveFromCsvToDataBase:"+ex.getMessage());
		}finally{
			Connector.closeConnection(connection);
			try{
				reader.close();
			}catch(Exception ex){}
		}
		
		return returnValue;
	}
	
	/** ������������� String � ������, ��� �������� ���������� ����� �� {@link java.sql.Types}
	 * @param value - ��������, ������� ����� ������������� � ���������� ������������� ������ 
	 * @param sqlType - java.sql.Types 
	 * @return ������, ������� ����� ���������
	 * @throws Exception, ���� ���������� �������� ������ � ���������� ���� 
	 * */
	private Object convertStringToObject(String value, int sqlType) throws Exception{
		Object returnValue=null;
		try{
			
			switch(sqlType){
				case Types.BIGINT:
				case Types.INTEGER:
				case Types.SMALLINT: {returnValue=Integer.valueOf(value);
				};break;//convert to Integer
				case Types.CHAR:
				case Types.LONGNVARCHAR:
				case Types.NCHAR:
				case Types.VARCHAR: {returnValue=value;
				
				};break; // convert to Varchar
				// �������� ����������� ��������� �� ������ ���� � ��������� ������� 
				case Types.TIME:
				case Types.TIMESTAMP:
				case Types.DATE:{
					returnValue=new java.sql.Date((new Date()).getTime());
				};break; // convert to Date

				case Types.FLOAT:
				case Types.DOUBLE:
				case Types.NUMERIC: {
					returnValue=Double.valueOf(value.replaceAll(",", "."));
				}; break; // convert to Float
				default: throw new Exception("not finded");
			}
		}catch(Exception ex){
			// convert Exception
		}
		return returnValue;
	}
	
	/** �������� � ����� ����� ������� */
	@SuppressWarnings("unused")
	private String getFileWithPrefix(String pathToFile, String prefix){
		File f=new File(pathToFile);
		if(f.getParent()!=null){
			if(f.getParent().equals(System.getProperty("file.separator"))){
				return System.getProperty("file.separator")+prefix+f.getName();
			}else{
				return f.getParent()+System.getProperty("file.separator")+prefix+f.getName();
			}
			
		}else{
			return prefix+f.getName();
		}
	}
	
}





/*Server: CLIENT_REQUEST_BONCARD_CATALOG
if(request.getTask().getName().equals("CLIENT_REQUEST_BONCARD_CATALOG")){
	ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"));
	DBFunction.fillIdentifier(identifier);
	String function_value=
	DBFunction.getStringFromDataBase(identifier,
									 0,
									 "CLIENT_BONCARD_CATALOG");
	LOGGER.debug(request.getTask().getName());
	request.getTask().clearData();
	request.getTask().addData(new Data(function_value));
	request.getTask().setStateIsDone();
	request.setDirectionFromServer();
};*/
/* Server: CLIENT_REQUEST_OPERATION_CATALOG
if(request.getTask().getName().equals("CLIENT_REQUEST_OPERATION_CATALOG")){
	ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"));
	DBFunction.fillIdentifier(identifier);
	String function_value=
	DBFunction.getStringFromDataBase(identifier,
									 0,
									 "CLIENT_OPERATION_CATALOG");
	LOGGER.debug(request.getTask().getName());
	request.getTask().clearData();
	request.getTask().addData(new Data(function_value));
	request.getTask().setStateIsDone();
	request.setDirectionFromServer();
};*/
/* : Server: CLIENT_REQUEST_SETTINGS_CATALOG
if(request.getTask().getName().equals("CLIENT_REQUEST_SETTINGS_CATALOG")){
	ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"));
	DBFunction.fillIdentifier(identifier);
	String function_value=
		DBFunction.getStringFromDataBase(identifier,
										 0,
										 "CLIENT_SETTINGS_CATALOG");
	LOGGER.debug(request.getTask().getName());
	request.getTask().clearData();
	request.getTask().addData(new Data(function_value));
	request.getTask().setStateIsDone();
	request.setDirectionFromServer();
};*/
/* : Server: CLIENT_RESPONSE_BONCARD_CATALOG
if(request.getTask().getName().equals("CLIENT_RESPONSE_BONCARD_CATALOG")){
	LOGGER.debug(request.getTask().getName());
	LOGGER.debug("set BonCard Catalog:"+request.getTask().getData().getObject());
	ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"));
	DBFunction.fillIdentifier(identifier);
	if(DBFunction.putObjectToDataBase(identifier,
									  0,
								      "CLIENT_BONCARD_CATALOG", 
								      request.getTask().getData().getObject())){
		request.getTask().clearData();
		request.getTask().setStateIsDone();	
	}else{
		request.getTask().clearData();
		request.getTask().setStateIsLOGGER.error();	
	}
	request.setDirectionFromServer();
};*/
/* : Server: CLIENT_RESPONSE_OPERATION_CATALOG
if(request.getTask().getName().equals("CLIENT_RESPONSE_OPERATION_CATALOG")){
	LOGGER.debug(request.getTask().getName());
	LOGGER.debug("set Operation Catalog:"+request.getTask().getData().getObject());
	ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"));
	DBFunction.fillIdentifier(identifier);
	if(DBFunction.putObjectToDataBase(identifier,
			   						  0,
			   						  "CLIENT_OPERATION_CATALOG", 
			   						  request.getTask().getData().getObject())){
		request.getTask().clearData();
		request.getTask().setStateIsDone();
	}else{
		request.getTask().clearData();
		request.getTask().setStateIsLOGGER.error();
	}
	request.setDirectionFromServer();
};*/

/*  CLIENT_RESPONSE_SETTINGS_CATALOG
if(request.getTask().getName().equals("CLIENT_RESPONSE_SETTINGS_CATALOG")){
	LOGGER.debug(request.getTask().getName());
	LOGGER.debug("set Settings Catalog:"+request.getTask().getData().getObject());
	ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"));
	DBFunction.fillIdentifier(identifier);
	if(DBFunction.putObjectToDataBase(identifier,
										  0,
			   						  "CLIENT_SETTINGS_CATALOG", 
			   						  request.getTask().getData().getObject())){
		request.getTask().clearData();
		request.getTask().setStateIsDone();
	}else{
		request.getTask().clearData();
		request.getTask().setStateIsLOGGER.error();
	}
	request.setDirectionFromServer();
};*/

/*  ���� �� ������ Server: Stub CLIENT_REQUEST_SETTINGS - (������������ ���� ���������� ����� �������� � ����������)
if(request.getTask().getName().equals("CLIENT_REQUEST_SETTINGS")){
	LOGGER.debug(request.getTask().getName());
	request.getTask().clearData();
	request.getTask().addData(new Data("file_01.bin",this.getZipBytesFromFile("d:\\temp.html")));
	request.getTask().setStateIsDone();
	request.setDirectionFromServer();
}*/

/* Server: Stub CLIENT_REQUEST_BONCARD - �������� ������ �� ������
if(request.getTask().getName().equals("CLIENT_REQUEST_BONCARD")){
	LOGGER.debug(request.getTask().getName());
	request.getTask().clearData();
	request.getTask().addData(new Data("file_boncard1.bin",this.getZipBytesFromFile("d:\\temp.html")));
	request.getTask().addData(new Data("file_boncard2.bin",this.getZipBytesFromFile("d:\\temp.html")));
	request.getTask().addData(new Data("file_boncard3.bin",this.getZipBytesFromFile("d:\\temp.html")));
	request.getTask().setStateIsDone();
	request.setDirectionFromServer();
}*/
/* Server: Stub CLIENT_RESPONSE_OPERATION - �������� �������� 
if(request.getTask().getName().equals("CLIENT_RESPONSE_OPERATION")){
	LOGGER.debug(request.getTask().getName());
	if(request.getTask().getData()!=null){
		try{
			
			String fileName=this.pathToTempDirectory+dateFilePrefix.format(new Date())+request.getTask().getData().getDataName();
			LOGGER.debug("file is getting,save file to "+fileName);
			if(this.getFileFromZipBytes((byte[])request.getTask().getData().getObject(), 
										 fileName)
										 ){
				ClientIdentifier identifier=new ClientIdentifier(request.getTransportParameter("login"),request.getTransportParameter("password"));
				DBFunction.fillIdentifier(identifier);
				
				if(getResultForSaveFileIntoDatbase(identifier,
								   				   request.getTask().getData().getDataName(),
								   				   fileName,
								   				   request.getTask().getVisualName()
												   )==true){
					info("file unzip OK:"+fileName);
					request.getTask().clearData();
					request.getTask().setStateIsDone();
					request.getTask().setInformation(":File Saved");
				}else{
					LOGGER.error("process file unzip Error:"+fileName);
					request.getTask().clearData();
					request.getTask().setStateIsLOGGER.error();
					request.getTask().setInformation(":Error in saving file");
				}
			}else{
				LOGGER.error("file unzip Error");
				request.getTask().clearData();
				request.getTask().setStateIsLOGGER.error();
				request.getTask().setInformation(":Error in getting file");
			}
		}catch(Exception ex){
			request.getTask().clearData();
			request.getTask().setStateIsLOGGER.error();
			LOGGER.error("  Exception:"+ex.getMessage());
			request.getTask().setInformation(":Package Error");
		}
	}else{
		LOGGER.error("error in getting file ");
		request.getTask().clearData();
		request.getTask().setStateIsLOGGER.error();
		request.getTask().setInformation(":Nothing for save");
	}
	request.setDirectionFromServer();
}*/

/*  Server: Stub CLIENT_REQUEST_LOG_BONCARD
if(request.getTask().getName().equals("CLIENT_REQUEST_LOG_BONCARD")){
	LOGGER.debug(request.getTask().getName());
	request.getTask().clearData();
	
	ArrayList<String> row_1=new ArrayList<String>();
	row_1.add("unique_file_identify_1");
	row_1.add("file_1.bin");
	row_1.add("2008.11.04");
	request.getTask().addData(new Data(row_1));

	ArrayList<String> row_2=new ArrayList<String>();
	row_2.add("unique_file_identify_2");
	row_2.add("file_2.bin");
	row_2.add("2008.11.04");
	request.getTask().addData(new Data(row_2));
	request.getTask().setStateIsDone();
	request.setDirectionFromServer();
}*/

/*  Server: Stub CLIENT_RESPONSE_OPERATION_LOG
if(request.getTask().getName().equals("CLIENT_RESPONSE_OPERATION_LOG")){
	LOGGER.debug(request.getTask().getName());
	request.getTask().clearData();
	ArrayList<String> row_1=new ArrayList<String>();
	row_1.add("unique_log_identify_1");
	row_1.add("log_1.bin");
	row_1.add("2008.11.04");
	request.getTask().addData(new Data(row_1));

	ArrayList<String> row_2=new ArrayList<String>();
	row_2.add("unique_log_identify_2");
	row_2.add("log2_2.bin");
	row_2.add("2008.11.04");
	request.getTask().addData(new Data(row_2));
	request.getTask().setStateIsDone();
	request.setDirectionFromServer();
}*/
// ��������, ��� �������, ����� ��������� ������������ �� XML ������ 
/*			if(request.getTask().getName().equals("GET_XML_CHECKER")){
				LOGGER.debug(request.getTask().getName());
				if(request.getTask().getDataCount()==1){
					// Server: GET_XML_FILE �������� ����������� ������ � ������
					// Server: ��������� ������, ���� ������ ������������
					LOGGER.debug(request.getTask().getData(0).getDataName()+" : "+request.getTask().getData(0).getObject());
					// ������ ����� �������
					request.getTask().clearData();
					request.getTask().addData(new Data("for_check.xml",
							                  		   this.getZipBytesFromFile("D:\\eclipse_workspace\\client_pattern_csv.xml ")
							                  		   )
					                          );
					// Data not valid
					//request.getTask().addData(new Data("for_access.xml",null));
					request.getTask().setStateIsDone();
					request.setDirectionFromServer();
				}else{
					LOGGER.error("GET_XML_CHECKER error TASK inner structure ");
				}
			}
*/			
