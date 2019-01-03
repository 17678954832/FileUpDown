// 上传电影内容
	@Value("${imageFilePath}")
	private String imageFilePath;

	@RequestMapping(value = "/ckeditorUpload")
	@ResponseBody
	public String ckeditorUpload(@RequestParam("upload") MultipartFile file,
			String CKEditorFuncNum) throws Exception {

		System.out.println(CKEditorFuncNum);
		// 获取文件名
		String fileName = file.getOriginalFilename();

		// 获取文件路径
		String suffixName = fileName.substring(fileName.lastIndexOf("."));

		// 重命名图片
		Date date = new Date();
		String newFileName = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(date) + suffixName;

		// 上传到指定路径
		FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
				imageFilePath + newFileName));

		StringBuffer sb = new StringBuffer();
		sb.append("<script type=\"text/javascript\">");
		sb.append("window.parent.CKEDITOR.tools.callFunction("
				+ CKEditorFuncNum + ",'" + "/image/" + newFileName + "','')");
		sb.append("</script>");
		return sb.toString();
	}

	// 上传图片
	@RequestMapping(value = "/addmovie")
	public String addmovie(Film film,
			@RequestParam("imageFile") MultipartFile file,
			HttpServletRequest request) throws Exception {
		Date date = new Date();
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			String a = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
			String newFileName = a + suffixName;
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
					imageFilePath + newFileName));
			film.setImageName(newFileName);
		}
		film.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(date));
		HttpSession session = request.getSession();
		String createUser = (String) session.getAttribute("userName");

		film.setCreateUser(createUser);
		System.out.println(createUser);
		filmService.addmovie(film);

		return "ManagePages/MovieManger";
	}