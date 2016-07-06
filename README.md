# -Java-EbookReader-Forum

An assignment for COMP3331 Computer Networks. 

Implementation of a eBook reader with a built in discussion forum. 

<b>Learning Object</b>
<ul>
  <li>Socket Programming</li>
  <li>Protocol and message design for applications</li>
  <li>Design of Network Application</li>
  <li>Push and pull protocols</li>
</ul>

<b>Procedure</b>

In one terminal, 
  'java Server (port_number)'
  
In one or more other terminal,
  'java Reader (protocol_mode) (polling_interval) (name) (hostname) (port_number)'

Possible commands for reader:

<b> Pull mode </b>
<ul>
  <li> display (book_name) (page_number) </li>
  <li> post_to_forum (page_number) (line_number) (string_of_content) </li>
  <li> read_post (line_number) </li>
</ul>
Note : post_to_forum and read_post should only be executed after selecting the book and page number in 'display'

<b> Push mode (To be implemented)</b>
<ul> 
  <li> The server forwards any new posts to the reader in push mode. </li>
  <li> display, post_to_forum, read_post as in pull mode
</ul>

<b> Notes </b> 
<ul>
  <li> The book pages have to be in the "src" folder for the initialisation to work. </li>
  <li> The server has to be started first before the reader(s) </li>
  <li> It is possible for multiple readers to be on different computers but they have to be in the same network. </li>
</ul>

<b> Reflection </b>
<ul>
  <li> This was my first time implementing threading in Java. It was surprisingly not too difficult. </li>
  <li> Timer turned out to be more complicated than I thought. It is still yet to be fully implemented correctly. </li>
  <li> IDEs are incredible at saving time. </li> 
</ul>
