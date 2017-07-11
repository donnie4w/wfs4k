# wfs4k 是用kotlin实现的是文件存储系统，主要是解决海量文件存储的问题,特别是小文件存储,原则上是简单易用,可扩展及备份恢复
## wfs4k是作者为实践kotlin而写的项目

***

# 介绍
### 主要功能与 github.com/donnie4w/wfs 完全一致。主要提供文件海量存储功能。
### 相应文件基础操作：文件存储，文件删除，文件查询


# 启动wfs 
**java -jar wfs4k.jar**
**参数说明： -max是上传文件大小限制（单位字节）   -p启动端口（默认3434）** 
	
使用wfs参考例子即可明白
# 1. 命令行
**上传文件** <br/>
(1)curl -F "file=@1.jpg" "http://127.0.0.1:3434/u"  <br/>
    **上传文件1.jpg 文件名 1.jpg** <br/>
(2)curl -F "file=@1.jpg" "http://127.0.0.1:3434/u/abc/11"   <br/>
    **上传文件1.jpg 文件名 abc/11** <br/>
例子(1)上传完成后访问文件 ：http://127.0.0.1:3434/r/1.jpg 	<br/>
例子(2)上传完成后访问文件 ：http://127.0.0.1:3434/r/abc/11   <br/>

**删除文件** <br/>
 curl -X DELETE "http://127.0.0.1:3434/d/1.jpg" <br/>    
 **删除文件 1.jpg**								<br/>
 curl -X DELETE "http://127.0.0.1:3434/d/abc/11"   <br/> 
 **删除文件 abc/11** 								<br/>

***

# 2. 使用thrift访问wfs4k   
  wfsPost()    上传文件 <br/>
  wfsRead()    拉取文件 <br/> 
  wfsDel       删除文件 <br/> 
可以参考go版本  github.com/donnie4w/wfs-goclient