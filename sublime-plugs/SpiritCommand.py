import sublime
import sublime_plugin
import urllib.parse
import urllib.request

class SpiritCommand(sublime_plugin.TextCommand):
	def run(self, edit):
		# 获取文件名
		filePath = self.view.file_name()
		print(filePath)
		# 获取光标位置
		point = self.view.sel()[0].a
		# 获取文件内容
		content = self.view.substr(sublime.Region(0, self.view.size()))
		strs = list(content)
		strs.insert(point, "@")
		content = "".join(strs)
		print(content)
		# 获取行号
		row, col = self.view.rowcol(point)
		print(row + 1)
		# 发送请求
		response = self.sendHttpRequest(filePath, content, row + 1)
		result = sublime.decode_value(response)
		print(result)
		# 解析返回的方法信息
		data = result["data"]
		items = []
		for item in data:
			items.append(item["tipText"])
		# 显示提示框
		self.edit = edit
		self.data = data
		self.view.show_popup_menu(items, self.on_done)

	def sendHttpRequest(self, filePath, content, lineNumber):
		url = "http://localhost:8729/spirit/kit/getMethodInfos"
		headers = { "Content-Type" : "application/json" }
		params = { "filePath" : filePath, "content" : content, "lineNumber" : lineNumber }
		jsonStr = sublime.encode_value(params, True)
		# print(jsonStr)
		request = urllib.request.Request(url, bytes(jsonStr, "utf8"), headers)
		response = urllib.request.urlopen(request)
		return response.read().decode("utf8")

	def on_done(self, index):
		if index >= 0:
			point = self.view.sel()[0].a
			self.view.insert(self.edit, point, self.data[index]["actualText"])
