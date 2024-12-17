
package dto;

import com.google.gson.annotations.Expose;


public class Response_DTO {
    
     @Expose
    private boolean success;
    
     @Expose
    private Object content;
      
      public Response_DTO(){}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
    
}
