package com.example.demo.MangeError;

import java.time.LocalDateTime;

public class ApiResponse {
	
		private String message ;
		private int code ;
		private LocalDateTime timestamp;
		public ApiResponse(String message, int code, LocalDateTime timestamp) {
			super();
			this.message = message;
			this.code = code;
			this.timestamp = timestamp;
		}
		public ApiResponse() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public LocalDateTime getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}
		@Override
		public String toString() {
			return "ApiError [message=" + message + ", code=" + code + ", timestamp=" + timestamp + "]";
		}
		

}