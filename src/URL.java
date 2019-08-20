class URL implements Comparable<URL>{
	
	public String url;
	
	public int nums;
	
	public URL(){}
	
	public URL(String url,int nums){
		
		this.url = url;
		
		this.nums = nums;
	}
	@Override
	public int compareTo(URL o) {
		
		if (this.url.equals(o.url)) {
			
			o.nums=this.nums+o.nums;
			
		}else {
			
			if (this.nums > o.nums) {
				
				return -1;
				
			}else{
				
				return 1;
				
			}
			
		}
		
		return 0;
		
	}
	
}
