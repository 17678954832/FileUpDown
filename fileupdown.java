// �ϴ���Ӱ����
	@Value("${imageFilePath}")
	private String imageFilePath;

	@RequestMapping(value = "/ckeditorUpload")
	@ResponseBody
	public String ckeditorUpload(@RequestParam("upload") MultipartFile file,
			String CKEditorFuncNum) throws Exception {

		System.out.println(CKEditorFuncNum);
		// ��ȡ�ļ���
		String fileName = file.getOriginalFilename();

		// ��ȡ�ļ�·��
		String suffixName = fileName.substring(fileName.lastIndexOf("."));

		// ������ͼƬ
		Date date = new Date();
		String newFileName = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(date) + suffixName;

		// �ϴ���ָ��·��
		FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
				imageFilePath + newFileName));

		StringBuffer sb = new StringBuffer();
		sb.append("<script type=\"text/javascript\">");
		sb.append("window.parent.CKEDITOR.tools.callFunction("
				+ CKEditorFuncNum + ",'" + "/image/" + newFileName + "','')");
		sb.append("</script>");
		return sb.toString();
	}

	// �ϴ�ͼƬ
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