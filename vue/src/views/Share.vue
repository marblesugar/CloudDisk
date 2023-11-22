<template>
  <div style="background-color: rgb(135,206,250,0.1);">
    <el-container style="height: 1000px;padding-top:50px;">
      <!--头像      -->
        <el-header height="150px" style="border: 2px solid balck;">

            <el-row>
                <!--头像 -->
                <el-col :span="1" :offset="4">
                    <el-dropdown @command="handleCommand">

                        <span class="el-dropdown-link" @click="shiftUserCenter">
                            <div class="demo-basic--circle">
                                <div class="block"><el-avatar :size="40" :src="userInfo.avatarUrl"></el-avatar></div>
                                <div class="sub-title"><b>{{ userInfo.username }}</b></div>
                            </div>
                        </span>

                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="logout">退出</el-dropdown-item>
                            <el-dropdown-item command="editAvatar">编辑头像</el-dropdown-item>
                        </el-dropdown-menu>

                        <!--退出     -->
                        <el-dialog title="提示"
                                   :visible.sync="dialogLogoutVisible"
                                   width="20%">
                            <span>确定退出吗？</span>

                            <span slot="footer" class="dialog-footer">
                                <el-button style="margin-left: 10px;" size="small" type="info" @click="dialogLogoutVisible = false">取 消</el-button>
                                <el-button style="margin-left: 10px;" size="small" type="danger" @click="logout">
                                    退出
                                </el-button>
                            </span>
                        </el-dialog>
                        <!--头像上传-->
                        <el-dialog title="修改头像"
                                   :visible.sync="dialogUploadAvatarVisible"
                                   width="40%">
                            <el-image :src="userInfo.avatarUrl" style="height: 150px"></el-image>
                            <el-upload v-loading="loading"
                                       class="upload"
                                       ref="upload"
                                       action=""
                                       :file-list="uploadAvatarList"
                                       :auto-upload="false"
                                       :on-change="handleChangeAvatar"
                                       :on-preview="handlePreview"
                                       :on-remove="handleRemove">
                                <el-button slot="trigger" size="small" type="primary" @click="delFile" icon="el-icon-thumb">
                                    选择文件
                                </el-button>

                            </el-upload>


                            <span slot="footer" class="dialog-footer">
                                <el-button style="margin-left: 10px;" size="small" type="info" @click="dialogUploadAvatarVisible = false">取 消</el-button>
                                <el-button style="margin-left: 10px;" size="small" type="success" @click="uploadAvatar" icon="el-icon-upload">
                                    更换
                                </el-button>
                            </span>
                        </el-dialog>

                    </el-dropdown>
                </el-col>
                <!--标题 -->
                <el-col :span="14">
                    <h1><i class="el-icon-share"></i>分享</h1>

                </el-col>
            </el-row>
            <!--处理多选            -->
            <el-row>
                <el-col :span="16" :offset="4">
                    <div v-show="multipleSelection.length !== 0">
                        <el-button v-if="op===1" size="mini" type="primary" icon="el-icon-download" @click="doSelect(1)">下载所选</el-button>
                    </div>
                </el-col>
            </el-row>

        </el-header>


      <el-main>
  
        <el-row>
         
<el-col :span="3"><i class="el-icon-cloudy"></i></el-col>
          <!--分页查询          -->
          <el-col :span="16" style="box-shadow: 5px 10px 30px rgb(0,0,0,0.1);">
            <el-table
                ref="tableData"
                :data="tableData"
                style="width: 100%"
                :row-class-name="tableRowClassName"
                @selection-change="handleSelectionChange">

              <el-table-column type="selection" width="55">
              </el-table-column>
               <!--序号 --> 
              <el-table-column
                  type="index"
                  label="序号"
                  width="100"
                  :index="(pageInfo.current-1)*pageInfo.size+1">
              </el-table-column>
              <!--文件名字 --> 
              <el-table-column
                  prop="fileName"
                  label="文件名"
                  width="180">
                <template scope="scope">
                  {{ scope.row.fileName }}
                  <el-button @click="editFileName(scope.row)" type="text" size="medium" v-if="op===1"><i class="el-icon-edit"></i>
                  </el-button>
                </template>
              </el-table-column>
                <!--文件类型 --> 
              <el-table-column
                  prop="fileType"
                  label="文件类型"
                  width="180">
                <template scope="scope">
                  {{ scope.row.isDir === 1 ? '文件夹' : scope.row.fileType }}
                </template>
              </el-table-column>
                <!--文件夹大小 --> 
              <el-table-column
                  label="文件(夹)大小">
                <template scope="scope">
                  {{ (scope.row.fileSize/1024.0).toFixed(2) + 'kb' }}
                </template>
              </el-table-column>

              <el-table-column
                  fixed="right"
                  label="操作"
                  width="300">
                  <template slot-scope="scope">


                      <el-button @click="download(scope.row)" type="text" size="medium" icon="el-icon-download" v-if="op===1&&scope.row.isDir===0">
                          下载
                      </el-button>

                  </template>

              </el-table-column>
            </el-table>
          </el-col>

        </el-row>
      </el-main>


      <el-footer>
          <el-row v-show="op===1||op===2">
              <!--
                   <el-col :span="2" :offset="4">
      <el-button type="primary" @click="createDir" size="small" icon="el-icon-folder-add" v-show="op===1">新建目录</el-button>
    </el-col>
      -->

              <el-col :span="6" :offset="op===1?6:12">
                  <el-pagination background
                                 @size-change="handleSizeChange"
                                 @current-change="handleCurrentChange"
                                 layout="sizes, prev, pager, next"
                                 :page-sizes="[3, 5, 7, 9]"
                                 :page-size="pageInfo.size"
                                 :total="pageInfo.total"
                                 :current-page="pageInfo.current">
                  </el-pagination>
              </el-col>
          </el-row>
      </el-footer>

    </el-container>
  </div>
</template>

<script>
import router from "@/router";

export default {
  data() {
        
    return {
      sendEmailLoading: false,
      loadingTime: 60,
      userInfo: {},
      userInfoForm: {},
      changePasswordForm: {},
      changeEmailForm: {},
      cancelAccountForm: {},
      tableData: [],
      multipleSelection: [],
      pageInfo: {
        total: 0,
        size: 0,
        current: 0,
        pages: 0
        },
      shareUrl:'',
      loading: false,
      uploadFileList: [],
      uploadAvatarList: [],
      curDir: '',
      dirList: [],
      fileKeyWord: '',
      searchTableData: [],
      dialogTableVisible: false,
      dialogShareVisible: false,
      dialogLogoutVisible: false,
      dialogUploadAvatarVisible: false,
      dialogUserFormVisible: false,
      dialogChangePasswordFormVisible: false,
      dialogChangeEmailFormVisible: false,
      dialogCancelAccountFormVisible: false,
      autoClearBin: 0,
      op: 1,

      ruleForm: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      },
      /*
      UserInfoFormRules: {
        username: [
          {required: true, message: '请输入您的用户名', trigger: 'blur'},
          {min: 2, max: 9, message: '长度在 2 到 9 个字符', trigger: 'blur'}
        ],
      },
      changeEmailFormStep2Rules: {
        email: [
          { type:'email', required: true, message: '请输入您的邮箱', trigger: 'blur' },
        ],
      },
      changePasswordFormRules: {
        oldPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
        newPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      },
      */
      step: 1
    }
  },
  watch: {
    'pageInfo.pages': {
      handler(newVal, oldVal) {
        if (Math.abs(newVal - oldVal) === 1 && op===1) {
          this.getPage(newVal, this.pageInfo.size)
        }
      }
    },
    'dialogChangeEmailFormVisible': {
      handler(){
        this.step = 1
      }
    },
    'dialogCancelAccountFormVisible':{
      handler(){
        this.step = 1
      }
    },
    'op':{
      handler(){
        this.fileKeyWord = ''
      }
    }
  },
  created() {
    this.getUserInfo()
    this.getPage(1, 5)
      var params = new URLSearchParams(window.location.search);
      var id = params.get("shareUrl");
      this.shareUrl = id;
      console.log("id=====share======" + id);

      if (id === "") {
          this.$router.push("http://localhost:8080/index");
      }

  },

  methods: {
    //注销账号
    cancelAccount(){
      console.log("注销请求")
      this.$http.get('/user/cancelAccount',{
        headers:{
          token: localStorage.getItem("token")
        }
      }).then(res=>{
        if (res.data.code === 200){
          this.$message.success(res.data.message)
          this.step = 3
          setTimeout(()=>{
            this.$router.push('/login')
          },3000)
        }else {
          this.$message.warning(res.data.message)
        }
      })
    },
    //发送验证码
    sendValidationCode(){
      var status = 1
      if (this.step === 1){
        status = 1
        if (this.dialogCancelAccountFormVisible){
          //注销账号
          status = 3
        }
      }else if (this.step === 2){
        var patten = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
        if (patten.test(this.changeEmailForm.email)){
          status = 2
        }else {
          this.$message.warning("邮箱格式不正确")
          return;
        }
      }
      this.$http.get('/user/sendValidationCode',{
        headers: {
          token: localStorage.getItem("token")
        },
        params: {
          'status': status,
          email: this.changeEmailForm.email
        }
      }).then(res=>{
        if (res.data.code===200){
          this.$message.success(res.data.message)
          this.sendEmailLoading = true
          var interval = setInterval(()=>{
            this.loadingTime-=1
            if (this.loadingTime === 0 || (this.step===2 && status===1) || this.step===3 ||(!this.dialogChangeEmailFormVisible&&!this.dialogCancelAccountFormVisible)){
              this.loadingTime = 60
              this.sendEmailLoading = false
              clearInterval(interval)
            }
          },1000)

        }else if (res.data.code===400){
          this.$message.error(res.data.message)
        }
      })
    },
    //打开修改密码的表单
    openChangePasswordDialog(){
      this.dialogChangePasswordFormVisible = true
      this.changePasswordForm = {}
    },
    //打开修改邮箱表单
    openChangeEmailDialog(){
      this.changeEmailForm = {}
      this.dialogChangeEmailFormVisible = true
    },
    //打开注销账号表单
    openCancelAccountDialog(){
      this.cancelAccountForm = {}
      this.dialogCancelAccountFormVisible = true
    },
    //提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if(formName === 'userInfoForm'){
            var form = JSON.parse(JSON.stringify(this.userInfoForm))
            form.sex = form.sex==='男'?0:1
            this.$http.post('/userInfo/changeInfo',form, {
              headers:{
                token: localStorage.getItem("token")
              }
            }).then(res=>{
              if(res.data.code === 200){
                // res.data.data.userInfo.avatarUrl += '?vision='+Math.random()
                // res.data.data.userInfo.sex=res.data.data.userInfo.sex===0?'男':'女'
                // this.userInfo = res.data.data.userInfo
                // this.userInfoForm = JSON.parse(JSON.stringify(this.userInfo))
                // console.log('userInfo==>')
                // console.log(this.userInfo)
                this.getUserInfo()
                this.dialogUserFormVisible = false
                this.$message.success("编辑成功！")
              }
            })
          }
          else if(formName === 'changePasswordForm'){
            if(this.changePasswordForm.newPassword !== this.changePasswordForm.confirmPassword){
              this.$message.error('两次密码不一致')
              return ;
            }else if(this.changePasswordForm.newPassword === this.changePasswordForm.oldPasswor){
              this.$message.error('新密码和旧密码一致')
              return ;
            }
            this.$http.post('/user/changePassword',this.changePasswordForm, {
              headers:{
                token: localStorage.getItem("token")
              }
            }).then(res=>{
              if (res.data.code === 200){
                this.dialogChangePasswordFormVisible = false
                this.$message.success(res.data.message)
              }else if (res.data.code === 400) {
                this.$message.error(res.data.message)
              }
            })
          }
          else if(formName === 'changeEmailFormStep1'){
            if (this.changeEmailForm.validationCode === undefined || this.changeEmailForm.validationCode === null ||this.changeEmailForm.validationCode === ''){
              this.$message.error("验证码不能为空")
              return;
            }
            this.$http.get('/user/verifyValidationCode',{
              headers:{
                token:localStorage.getItem("token")
              },
              params:{
                "validationCode": this.changeEmailForm.validationCode,
                "status": 1
              }
            }).then(res=>{
              if(res.data.code===200){
                this.$message.success(res.data.message)
                this.changeEmailForm={}
                this.step=2
              }else if (res.data.code === 400){
                this.$message.error(res.data.message)
              }
            })
          }
          else if(formName === 'changeEmailFormStep2'){
            var patten = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
            if (!patten.test(this.changeEmailForm.email)){
              this.$message.warning("邮箱格式不正确")
              return;
            }else if (this.changeEmailForm.validationCode === undefined || this.changeEmailForm.validationCode === null ||this.changeEmailForm.validationCode === ''){
              this.$message.error("验证码不能为空")
              return;
            }
            this.$http.post('/user/changeEmail',this.changeEmailForm, {
              headers:{
                token: localStorage.getItem("token")
              }
            }).then(res=>{
              if(res.data.code===200){
                this.$message.success(res.data.message)
                this.changeEmailForm = {}
                this.getUserInfo()
                this.step = 3
              }else if(res.data.code === 400){
                this.$message.error(res.data.message)
              }
            })
          }
          else if(formName === 'cancelAccountForm'){
            if (this.cancelAccountForm.validationCode === undefined || this.cancelAccountForm.validationCode === null ||this.cancelAccountForm.validationCode === ''){
              console.log('123')
              this.$message.error("验证码不能为空")
              return;
            }
            this.$http.get('/user/verifyValidationCode',{
              headers:{
                token:localStorage.getItem("token")
              },
              params:{
                "validationCode": this.cancelAccountForm.validationCode,
                "status": 3
              }
            }).then(res=>{
              if(res.data.code===200){
                this.$message.success(res.data.message)
                this.cancelAccountForm={}
                this.step=2
              }else if (res.data.code === 400){
                this.$message.error(res.data.message)
              }
            })
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    //重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    //获取用户信息{
    getUserInfo() {
      this.$http.get('/userInfo', {
        headers: {
          token: localStorage.getItem("token")
        }
      }).then(res => {
        if (res.data.code === 200) {
          var userInfo = res.data.data.userInfo
          userInfo.avatarUrl += '?vision='+Math.random()
          if(typeof (userInfo.sex) === "number"){
            userInfo.sex = userInfo.sex===0?'男':'女'
          }
          //TODO 日期有bug elem控件选择后少一天,使用了网上的解决方法也没有效果
          userInfo.birthDay = userInfo.birthDay==null?'':userInfo.birthDay.slice(0,10)
          this.userInfo = userInfo
          this.userInfoForm = JSON.parse(JSON.stringify(this.userInfo))
          console.log('userInfo==>')
          console.log(this.userInfo)
        } else if (res.data.code === 400) {
          this.$message.error(res.data.message)
        }
      })
    },
    //分页查询
      getPage(currentPage, pageSize) {
       var params = new URLSearchParams(window.location.search);
      console.log(params.get("shareUrl"));
      this.$http({
          url: '/share/' + params.get("shareUrl"),
        headers: {
          'token': localStorage.getItem("token")
        },
        method: 'GET',
        params: {
        }
      }).then(res => {
        console.log(res.data);
        this.tableData= res.data.data.file
        
      })
    },
    // 处理多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 操作多选
    doSelect(opSelect){
      //下载
      if (opSelect === 1){
        for (var val of this.multipleSelection){
          this.download(val)
        }
        this.$refs.tableData.clearSelection()
      }else if (opSelect === 2){//恢复
        this.recovery(this.multipleSelection)
      }
      // TODO 批量删除
      else if (opSelect === 3){//删除
        if (this.op===1){//主页删除
          this.deleteList(this.multipleSelection)
        }else if (this.op===2){//回收站删除
          this.deleteListForever(this.multipleSelection)
        }
      }
    },
    //主页
    shiftIndex(){
      this.multipleSelection = []
      this.op=1
      this.getPage(1, 5)
    },
    //回收站
    shiftRecycle(){
      this.multipleSelection = []
      if (this.autoClearBin === 0){
        this.deleteExpiredFile();
        this.$message.success("已自动清理过期文件");
        this.autoClearBin = 1
      }
      this.op=2
      this.getPage(1,5)
      },
     //我的分享
    //未完成ing
      shiftShare() {
          this.multipleSelection = []
          this.op = 3
          
      },
    //个人中心
    shiftUserCenter(){
      this.multipleSelection = []
      document.querySelector("#userCenter").click()
      this.op=4
    },
    //隔行变色
    tableRowClassName({row, rowIndex}) {
      if (rowIndex % 4 === 1) {
        return 'warning-row';
      } else if (rowIndex % 4 === 3) {
        return 'success-row';
      }
      return '';
    },
    //用户操作
    handleCommand(command) {
      if (command === 'logout') {
        this.dialogLogoutVisible=true
      }
      if (command === 'editAvatar') {
        this.dialogUploadAvatarVisible=true
      }
    },
    //退出
    logout(){
      this.$message.info('已退出！');
      localStorage.removeItem("token");
      this.$router.push("/login")
    },
    //换页
    handleCurrentChange(currentPage) {
      this.getPage(currentPage, this.pageInfo.size)
    },
    //每页条数改变
    handleSizeChange(pageSize) {
      this.getPage(1, pageSize)
    },
    //下载
    download(row) {
      console.log(row.id)
      if (row.isDir === 1){
        this.$message.warning("文件夹不能下载喔")
        return;
      }
      this.$http.get("/file/download/" + row.id, {
        //!!!!!解决文档图片乱码
        responseType: "arraybuffer",
        headers: {
          token: localStorage.getItem("token")
        }
      }).then(res => {
        const blob = new Blob([res.data]);
        const elink = document.createElement('a');
        elink.download = row.fileName;
        elink.style.display = 'none';
        elink.href = URL.createObjectURL(blob);
        document.body.appendChild(elink);
        elink.click();
        URL.revokeObjectURL(elink.href); // 释放URL 对象
        document.body.removeChild(elink);
      })
      },
    //分享文件，产生分享链接
    shareSingle(row) {
       //未完成！！！！！！
        console.log("分享时的信息" + row.id);
        console.log("分享时item的信息" + this.curDir);
        console.log(this);
        
        this.$http.get('/shareFile/'+row.id, {
            headers: {
                token: localStorage.getItem("token")
            }
        }).then(res => {
            console.log("前端成功收到result" + res.data.data.shareUrl);
            this.shareUrl = "http://localhost:8080/share?shareUrl=" + res.data.data.shareUrl;
            this.dialogShareVisible = true
        })
        
      },
    //分享链接复制
      copy() {
          var input = document.createElement("input"); // 创建input对象
          input.value = this.shareUrl; // 设置复制内容
          document.body.appendChild(input); // 添加临时实例
          input.select(); // 选择实例内容
          document.execCommand("Copy"); // 执行复制
          document.body.removeChild(input); // 删除临时实例
          this.$message.success('复制成功！');
      },
    //选择文件时,清空fileList
    delFile() {
      this.uploadFileList = [];
      this.uploadAvatarList = []
    },
    handleChange(file, fileList) {
      this.uploadFileList = fileList;

    },
    handleChangeAvatar(file, fileList) {
      this.uploadAvatarList = fileList
    },
    //删除准备上传的文件
    handleRemove(file, uploadFileList) {
      this.uploadFileList = [];
      this.uploadAvatarList = [];
    },
    // 点击文件
    handlePreview(file) {
      console.log(file);
    },
    //头像上传前判断
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    //自定义上传文件
    upload() {
      if (this.uploadFileList.length === 0){
        this.$message.warning("未选择文件")
        return;
      }
      let formData = new FormData()
      formData.append("uploadFile", this.uploadFileList[0].raw)//拿到存在fileList的文件存放到formData中
      //下面数据是我自己设置的数据,可自行添加数据到formData(使用键值对方式存储)
      formData.append("curDir", this.curDir)
      this.loading = true
      this.$http.post("/file/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data;charset=utf-8",
          token: localStorage.getItem("token")
        },
      }).then(res => {
        if (res.data.code === 200) {
          this.$refs.upload.submit()
          this.$message.success(res.data.message)
          //this.getPage(this.pageInfo.current,this.pageInfo.size)
          this.getPage(this.pageInfo.pages, this.pageInfo.size)
        }
      }).finally(res => {
        this.loading = false
      }).catch(err => {
        this.$message.error("文件上传失败")
      })
    },
    uploadAvatar(){
      let formData = new FormData()
      var file = this.uploadAvatarList[0].raw
      formData.append("avatarFile", file)//拿到存在fileList的文件存放到formData中
      if (!this.beforeAvatarUpload(file)){
        return;
      }
      this.loading = true
      this.$http.post("/userInfo/uploadAvatar", formData, {
        headers: {
          "Content-Type": "multipart/form-data;charset=utf-8",
          token: localStorage.getItem("token")
        },
      }).then(res => {
        if (res.data.code === 200) {
          this.$refs.upload.submit()
          this.$message.success(res.data.message)
          console.log(res.data.data.avatarUrl)
          this.userInfo.avatarUrl = res.data.data.avatarUrl + '?vision='+Math.random()
          this.dialogUploadAvatarVisible = false
        }
      }).finally(res => {
        this.loading = false
      }).catch(err => {
        this.$message.error("头像上传失败")
      })
    },
    //修改文件名
    editFileName(row) {
      var message = ''
      var inputPattern = ''
      var inputErrorMessage = ''
      if (row.isDir === 1) {
        message = '请输入新文件夹名'
        inputPattern = /^(?!.*(\/)|(\\))/
        inputErrorMessage = '目录中不能包含斜杠'
      } else if (row.isDir === 0) {
        message = '请输入新文件名'
        inputPattern = /^\S+$/
        inputErrorMessage = '不能为空'
      }

      this.$prompt(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: inputPattern,
        inputErrorMessage: inputErrorMessage
      }).then(({value}) => {
        if (!value) {
          this.$message({
            type: 'error',
            message: '文件名不能为空'
          });
        } else {
          this.$http.put('/file/' + row.id + '?newName=' + value, {}, {
            headers: {
              token: localStorage.getItem("token")
            }
          }).then(res => {
            if (res.data.code === 200) {
              this.$message({
                type: 'success',
                message: res.data.message
              })
              this.getPage(this.pageInfo.current, this.pageInfo.size)
            } else {
              this.$message({
                type: 'error',
                message: res.data.message
              });
            }
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消修改'
        });
      });
    },
    //新建目录
    createDir() {
      this.$prompt('请输入文件夹名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^(?!.*(\/)|(\\))/,
        inputErrorMessage: "目录中不能包含斜杠"
      }).then(({value}) => {
        if (!value) {
          this.$message({
            type: 'error',
            message: '文件名不能为空'
          });
        } else {
          this.$http.post('/file/createDir', {
            curDir: this.curDir !== '' ? this.curDir : '/',
            dirName: value
          }, {
            headers: {
              token: localStorage.getItem("token")
            }
          }).then(res => {
            if (res.data.code === 200) {
              this.$message({
                type: 'success',
                message: value + '创建成功！'
              })
              this.getPage(this.pageInfo.current, this.pageInfo.size)
            } else {
              this.$message({
                type: 'error',
                message: res.data.message
              });
            }
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消创建'
        });
      });
    },
    //进入文件夹
    enterDir(row) {
      this.dirList.push({id: row.id, dirName: row.fileName, returnDirCurPage: this.pageInfo.current})
      this.curDir += "/" + row.fileName
      console.log("上级目录所在页数" + this.dirList.slice(-1)[0].returnDirCurPage)
      this.getPage(1, this.pageInfo.size)
      console.log("进入目录")
      console.log("dirList=" + this.dirList)
      console.log("curDir=" + this.curDir)
    },
    //返回上一级目录
    returnDir() {
      var popDir = this.dirList.pop()
      if (this.dirList.length === 0) {
        this.curDir = ''
      } else {
        this.curDir = this.curDir.substring(0, this.curDir.length - (popDir.dirName.length + 1))
      }
      console.log("应该所在的页码" + popDir.returnDirCurPage)
      this.getPage(popDir.returnDirCurPage, this.pageInfo.size)
      console.log("返回目录")
      console.log("dirList=" + this.dirList)
      console.log("curDir=" + this.curDir)
    },
    //去往主目录
    toIndexDir() {
      this.curDir = ''
      this.dirList = []
      this.getPage(1, this.pageInfo.size)
    },
    //去往指定文件夹
    enterDirById(tid) {
      var list = []
      this.curDir = ''
      for (var {id, dirName} of this.dirList) {
        this.curDir += "/" + dirName
        list.push({id, dirName})
        if (id === tid) {
          break
        }
      }
      this.dirList = list
      console.log(this.curDir)
      console.log(this.dirList)
      this.getPage(1, this.pageInfo.size)
      // this.$http.get('/file/getFileCurDir/'+id,{
      //   headers:{
      //     token: localStorage.getItem("token")
      //   }
      // }).then(res=>{
      //   var relativeDir = res.data.data.relativeDir
      //   if(relativeDir === '/'){
      //     this.curDir=relativeDir+dirName
      //     this.dirList = [dirName]
      //   }else {
      //     var dir = relativeDir+'/'+dirName
      //     this.curDir=dir
      //     this.dirList = dir.slice(1).split("/")
      //   }
      //   console.log("curDir="+this.curDir)
      //   console.log("dirList="+this.dirList)
      // })
    },
    //在此目录中进行搜索
    searchFileInCurDir() {
      if (this.fileKeyWord === ''){
        this.$message.warning("请输入文件关键字")
        return;
      }
      this.$http.get('/file/searchFileInCurDir', {
        headers: {
          token: localStorage.getItem("token")
        },
        params: {
          curDir: this.curDir,
          fileKeyWord: this.fileKeyWord
        }
      }).then(res => {
        this.searchTableData = res.data.data.searchFileList
        this.dialogTableVisible = true
      })
      },

    //永久删除回收站中的所有文件
    deleteAllForever(){
      this.$confirm('此操作将永久删除回收站中的所有文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete('/file/deleteAllForever',{
          headers:{
            token: localStorage.getItem("token")
          }
        }).then(res=>{
          this.$message({
            type: 'success',
            message: res.data.message
          });
          this.getPage(1,this.pageInfo.size)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //清空回收站中过期文件
    deleteExpiredFile(){
      this.$http.delete('/file/deleteExpiredFile',{
        headers:{
          token: localStorage.getItem("token")
        }
      }).then(res=>{
        console.log(res)
      })
    },
    //永久删除回收站的某个文件
    deleteSingleForever(row){
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete('/file/deleteSingleForever/'+row.id,{
          headers:{
            token: localStorage.getItem("token")
          }
        }).then(res=>{
          this.$message({
            type: 'success',
            message: res.data.message
          });
          this.getPage(this.pageInfo.current,this.pageInfo.size)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //主页文件删除
    /*
    deleteFile(row) {
      this.autoClearBin = 0
      this.$confirm('此操作将临时删除该文件, 7天内可在回收站中还原', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.delete("/file/" + row.id, {
          headers: {
            token: localStorage.getItem("token")
          }
        }).then(res => {
          if (res.data.code === 200) {
            this.$message.success(res.data.message)
          }
        }).catch(res => {
          this.$message.error(res.data.message)
        }).finally(res => {
          setTimeout(() => {
            this.getPage(this.pageInfo.current, this.pageInfo.size)
          }, 500)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });


      },
    */
    //主页批量删除
    deleteList(rows){
      this.autoClearBin = 0
      var ids = []
      var message = null
      if (Object.prototype.toString.call(rows) === '[object Array]'){//多个恢复
        message="是否删除【"
        for (var row of rows){
          ids.push(row.id)
          message+=row.fileName+","
        }
        message = message.slice(0,-1)
        message+="】文件, 7天内可在回收站中还原"
      }
      console.log(ids)
      console.log(message)

      this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.delete('/file/deleteList',{
          headers:{
            token: localStorage.getItem("token")
          },
          data:ids
        }).then(res=>{
          this.$refs.tableData.clearSelection()
          this.getPage(this.pageInfo.current,this.pageInfo.size)
        }).catch(res => {
          this.$message.error(res.data.message)
        }).finally(res => {
          setTimeout(() => {
            this.getPage(this.pageInfo.current, this.pageInfo.size)
          }, 500)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });



    },
    //永久删除
    deleteListForever(rows){
      var ids = []
      var message = null
      if (Object.prototype.toString.call(rows) === '[object Array]'){//多个恢复
        message="是否永久删除【"
        for (var row of rows){
          ids.push(row.id)
          message+=row.fileName+","
        }
        message = message.slice(0,-1)
        message+="】文件,此操作不可逆"
      }
      this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.delete('/file/deleteListForever',{
          headers:{
            token: localStorage.getItem("token")
          },
          data:ids
        }).then(res=>{
          this.$refs.tableData.clearSelection()
          this.getPage(this.pageInfo.current,this.pageInfo.size)
        }).catch(res => {
          this.$message.error(res.data.message)
        }).finally(res => {
          setTimeout(() => {
            this.getPage(this.pageInfo.current, this.pageInfo.size)
          }, 500)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //通过id恢复回收站中的文件
    recovery(row){
      var ids = []
      var message = null
      if (Object.prototype.toString.call(row) === '[object Object]'){//如果是单个恢复
        ids.push(row.id)
        message="是否恢复【"+row.fileName+"】文件"
      }else if (Object.prototype.toString.call(row) === '[object Array]'){//多个恢复
        console.log("hhhhh")
        message="是否恢复【"
        for (var item of row){
          ids.push(item.id)
          message+=item.fileName+","
        }
        message = message.slice(0,-1)
        message+="】文件"
      }
      this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.put('/file/recovery/',ids,{
          headers:{
            token: localStorage.getItem("token")
          }
        }).then(res=>{
          this.$message({
            type: 'success',
            message: res.data.message
          });
          this.$refs.tableData.clearSelection()
          this.getPage(this.pageInfo.current,this.pageInfo.size)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
  }
}
</script>


<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}


.el-dropdown-link {
  cursor: pointer;
  color: black;
}

.el-icon-arrow-down {
  font-size: 12px;
}

</style>