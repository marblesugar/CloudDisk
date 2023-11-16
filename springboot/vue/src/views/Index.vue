<template>
  <div>
    <el-container style="height: 600px">
      <!--头像      -->
      <el-header height="150px">

        <el-row>
          <!--头像          -->
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
              <el-dialog
                  title="提示"
                  :visible.sync="dialogLogoutVisible"
                  width="20%">
                <span>确定退出吗？</span>

                <span slot="footer" class="dialog-footer">
                  <el-button style="margin-left: 10px;" size="small" type="info" @click="dialogLogoutVisible = false">取 消</el-button>
                  <el-button style="margin-left: 10px;" size="small" type="danger" @click="logout" >
                    退出
                  </el-button>
                </span>
              </el-dialog>
              <!--头像上传-->
              <el-dialog
                  title="修改头像"
                  :visible.sync="dialogUploadAvatarVisible"
                  width="40%">
                <el-image :src="userInfo.avatarUrl" style="height: 150px"></el-image>
                <el-upload
                    v-loading="loading"
                    class="upload"
                    ref="upload"
                    action=""
                    :file-list="uploadAvatarList"
                    :auto-upload="false"
                    :on-change="handleChangeAvatar"
                    :on-preview="handlePreview"
                    :on-remove="handleRemove">
                  <el-button slot="trigger" size="small" type="primary" @click="delFile" icon="el-icon-thumb">选择文件
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
          <!--标题          -->
          <el-col :span="14">
            <h1 v-show="op===1"><i class="el-icon-s-home"></i>主页</h1>
            <h1 v-show="op===2"><i class="el-icon-delete-solid"></i>回收站</h1>
            <h1 v-show="op===3"><i class="el-icon-user-solid"></i>个人中心</h1>
          </el-col>
        </el-row>
        <!--搜索结果弹出表格        -->
        <el-dialog title="搜索结果" :visible.sync="dialogTableVisible">
          <el-table :data="searchTableData">
            <el-table-column
                type="index"
                label="序号"
                width="100">
            </el-table-column>
            <el-table-column
                prop="fileName"
                label="文件名"
                width="150">
              <template scope="scope">
                {{ scope.row.fileName }}
                <el-button @click="editFileName(scope.row)" type="text" size="medium"><i class="el-icon-edit"></i>
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
                prop="fileType"
                label="文件类型"
                width="150">
              <template scope="scope">
                {{ scope.row.isDir === 1 ? '文件夹' : scope.row.fileType }}
              </template>
            </el-table-column>
            <el-table-column
                label="文件大小">
              <template scope="scope">
                {{ scope.row.fileSize + 'bytes' }}
              </template>
            </el-table-column>
            <el-table-column
                fixed="right"
                label="操作"
                width="200">
              <template slot-scope="scope">
                <el-button @click="download(scope.row)" type="text" size="medium" icon="el-icon-download">下载</el-button>
                <el-button @click="deleteFile(scope.row)" type="text" size="medium" icon="el-icon-delete">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-dialog>
        <!--搜索        -->
        <el-row v-show="op===1">
          <el-col :span="6" :offset="8">
            <el-input
                placeholder="在此目录中进行搜索"
                v-model="fileKeyWord"
                clearable
                @keyup.enter.native="searchFileInCurDir">
            </el-input>
          </el-col>
          <el-col :span="2">
            <el-button type="success" round icon="el-icon-search" @click="searchFileInCurDir">搜索</el-button>
          </el-col>

          <!--文件上传        -->
          <el-col :span="4">
            <el-upload
                v-loading="loading"
                class="upload"
                ref="upload"
                action=""
                :file-list="uploadFileList"
                :auto-upload="false"
                :on-change="handleChange"
                :on-preview="handlePreview"
                :on-remove="handleRemove">
              <el-button slot="trigger" size="small" type="primary" @click="delFile" icon="el-icon-thumb" v-show="op===1">选择文件
              </el-button>
              <el-button style="margin-left: 10px;" size="small" type="success" @click="upload" icon="el-icon-upload" v-show="op===1">
                上传到服务器
              </el-button>
            </el-upload>

          </el-col>
        </el-row>
        <!--目录        -->
        <el-row>
          <el-col :span="16" :offset="4">
            <div v-show="op===1">
              <div style="float: left">
                <el-link type="primary" icon="el-icon-s-home" @click="toIndexDir" :underline="false">主目录</el-link>
                <el-link v-for="item in dirList" type="primary" icon="el-icon-arrow-right" @click="enterDirById(item.id)"
                         :underline="false">{{ item.dirName }}
                </el-link>
              </div>
              <div v-show="dirList.length!=0" style="float: right">
                <el-link type="primary" icon="el-icon-back" @click="returnDir">返回</el-link>
              </div>
            </div>
            <div v-show="op===2" style="float: left">
              <el-link type="danger" icon="el-icon-delete-solid" @click="deleteAllForever" :underline="false">删除所有文件</el-link>
            </div>
          </el-col>
        </el-row>
        <!--处理多选            -->
        <el-row>
          <el-col :span="16" :offset="4">
            <div v-show="multipleSelection.length !== 0">
              <el-button v-if="op===1" size="mini" type="primary" icon="el-icon-download" @click="doSelect(1)" >下载所选</el-button>
              <el-button v-else size="mini" type="primary" icon="el-icon-refresh-left" @click="doSelect(2)" >恢复所选</el-button>
              <el-button type="danger" size="mini" icon="el-icon-delete" @click="doSelect(3)" >删除所选</el-button>
            </div>
          </el-col>
        </el-row>

      </el-header>


      <el-main>
        <el-row>
          <el-col :span="4">
            <h3><i class="el-icon-location"></i> 导航</h3>
            <el-menu
                ref="menu"
                default-active="1"
                class="el-menu-vertical-demo"
                @open="handleOpen"
                @close="handleClose">
              <el-menu-item index="1" @click="shiftIndex">
                <template slot="title">
                  <i :class="op===1?'el-icon-s-promotion':'el-icon-house'"></i>
                  <span>主页</span>
                </template>
              </el-menu-item>
              <el-menu-item index="2" @click="shiftRecycle">
                <i :class="op===2?'el-icon-s-promotion':'el-icon-delete'"></i>
                <span slot="title">回收站</span>
              </el-menu-item>
              <el-menu-item id="userCenter" index="3" @click="shiftUserCenter">
                <i :class="op===3?'el-icon-s-promotion':'el-icon-user'"></i>
                <span slot="title">个人中心</span>
              </el-menu-item>
            </el-menu>
          </el-col>

          <!--分页查询          -->
          <el-col :span="16" v-show="op===1||op===2">
            <el-table
                ref="tableData"
                :data="tableData"
                style="width: 100%"
                :row-class-name="tableRowClassName"
                @selection-change="handleSelectionChange">
              <el-table-column
                  type="selection"
                  width="55">
              </el-table-column>
              <el-table-column
                  type="index"
                  label="序号"
                  width="100"
                  :index="(pageInfo.current-1)*pageInfo.size+1">
              </el-table-column>
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
              <el-table-column
                  prop="fileType"
                  label="文件类型"
                  width="180">
                <template scope="scope">
                  {{ scope.row.isDir === 1 ? '文件夹' : scope.row.fileType }}
                </template>
              </el-table-column>
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
                  <el-button @click="deleteFile(scope.row)" type="text" size="medium" icon="el-icon-delete" v-if="op===1" style="color: rgb(245,108,108)">
                    删除
                  </el-button>
                  <el-button @click="deleteSingleForever(scope.row)" type="text" size="medium" icon="el-icon-delete" v-else-if="op===2" style="color: rgb(245,108,108)">
                    永久删除
                  </el-button>
                  <el-button @click="download(scope.row)" type="text" size="medium" icon="el-icon-download" v-if="op===1&&scope.row.isDir===0">
                    下载
                  </el-button>
                  <el-button @click="recovery(scope.row)" type="text" size="medium" icon="el-icon-refresh-left" v-else-if="op===2">
                    恢复
                  </el-button>
                  <el-button v-if="scope.row.isDir===1&&op===1" @click="enterDir(scope.row)" type="text" size="medium"
                             icon="el-icon-folder-opened">进入文件夹
                  </el-button>

                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <!--用户中心          -->
          <el-col :span="16" v-show="op===3">
            <!--头像-->
            <el-row>
              <el-col :span="16" :offset="4">
                <span class="el-dropdown-link" @click="dialogUploadAvatarVisible=true">
                    <div class="demo-basic--circle">
                      <div class="block">
                        <el-avatar :size="100" :src="userInfo.avatarUrl"></el-avatar>
                      </div>
                    </div>
              </span>
              </el-col>
            </el-row>
            <!--用户信息表单-->
            <el-row>
              <el-dialog title="用户信息" :visible.sync="dialogUserFormVisible">
                <el-form :model="userInfoForm" :rules="UserInfoFormRules" ref="userInfoForm" label-width="100px" class="demo-ruleForm">
                  <el-form-item label="用户名" prop="username">
                    <el-input v-model="userInfoForm.username" style="width: 40%;"></el-input>
                  </el-form-item>
                  <el-form-item label="邮箱">
                    <el-input v-model="userInfoForm.email" disabled style="width: 40%;"></el-input>
                  </el-form-item>
                  <el-form-item label="生日" prop="birthDay">
                    <el-date-picker type="date" placeholder="选择日期" v-model="userInfoForm.birthDay" style="width: 40%;" value-format="yyyy-MM-dd"></el-date-picker>
                  </el-form-item>
                  <el-form-item label="性别" prop="sex">
                    <el-radio-group v-model="userInfoForm.sex">
                      <el-radio label="男" value="0"></el-radio>
                      <el-radio label="女" value="1"></el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="活动区域" prop="country">
                    <el-select v-model="userInfoForm.country" placeholder="请选择国家" style="width: 40%;">
                      <el-option label="中国" value="中国"></el-option>
                      <el-option label="美国" value="美国"></el-option>
                      <el-option label="英国" value="英国"></el-option>
                      <el-option label="法国" value="法国"></el-option>
                      <el-option label="俄罗斯" value="俄罗斯"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="民族" prop="nation">
                    <el-input v-model="userInfoForm.nation" placeholder="填写您的民族" style="width: 40%;"></el-input>
                  </el-form-item>
                  <el-form-item label="职业" prop="occupation">
                    <el-input v-model="userInfoForm.occupation" placeholder="填写您的职业" style="width: 40%;"></el-input>
                  </el-form-item>
                  <el-form-item label="工作单位" prop="workPlace">
                    <el-input v-model="userInfoForm.workPlace" placeholder="填写您的工作单位" style="width: 40%;"></el-input>
                  </el-form-item>
                  <el-form-item label="居住地" prop="homePlace">
                    <el-input v-model="userInfoForm.homePlace" placeholder="填写您的居住地" style="width: 40%;"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button @click="resetForm('userInfoForm')">重置</el-button>
                  </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="dialogUserFormVisible = false">取 消</el-button>
                  <el-button type="primary" @click="submitForm('userInfoForm')">确定</el-button>
                </div>
              </el-dialog>
              <!--用户信息-->
              <el-col :span="16" :offset="4">
                <el-descriptions class="margin-top" title="您的信息" :column="3" :size="size">
                  <template slot="extra">
                    <el-button type="warning" size="small" @click="dialogUserFormVisible=true" icon="el-icon-edit" plain>编辑</el-button>
                  </template>
                  <el-descriptions-item label="用户名">
                    <el-tag>{{ userInfo.username }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="邮箱">
                    <el-tag>{{ userInfo.email }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="生日">
                    <el-tag :type="userInfo.birthDay===null||userInfo.birthDay===''?'success':''">{{ userInfo.birthDay===null||userInfo.birthDay===''?'暂未填写':userInfo.birthDay }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="性别">
                    <el-tag :type="userInfo.sex===null||userInfo.sex===''?'success':''">{{ userInfo.sex===null||userInfo.sex===''?'暂未填写':userInfo.sex }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="国家">
                    <el-tag :type="userInfo.country===null||userInfo.country===''?'success':''">{{ userInfo.country===null||userInfo.country===''?'暂未填写':userInfo.country }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="民族">
                    <el-tag :type="userInfo.nation===null||userInfo.nation===''?'success':''">{{ userInfo.nation===null||userInfo.nation===''?'暂未填写':userInfo.nation }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="职业">
                    <el-tag :type="userInfo.occupation===null||userInfo.occupation===''?'success':''">{{ userInfo.occupation===null||userInfo.occupation===''?'暂未填写':userInfo.occupation }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="工作单位">
                    <el-tag :type="userInfo.workPlace===null||userInfo.workPlace===''?'success':''">{{ userInfo.workPlace===null||userInfo.workPlace===''?'暂未填写':userInfo.workPlace }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="居住地">
                    <el-tag :type="userInfo.homePlace===null||userInfo.homePlace===''?'success':''">{{ userInfo.homePlace===null||userInfo.homePlace===''?'暂未填写':userInfo.homePlace }}</el-tag>
                  </el-descriptions-item>
                </el-descriptions>
              </el-col>
            </el-row>

           <el-row>
             <!--修改用户密码-->
             <el-button type="warning" plain @click="openChangePasswordDialog" icon="el-icon-key" style="margin: 5px">修改密码</el-button>
             <el-dialog title="修改密码" :visible.sync="dialogChangePasswordFormVisible" width="500px">
               <el-form :model="changePasswordForm" :rules="changePasswordFormRules" ref="changePasswordForm" label-width="100px" class="demo-ruleForm">
                 <el-form-item label="旧密码" prop="oldPassword">
                   <el-input show-password v-model="changePasswordForm.oldPassword" ></el-input>
                 </el-form-item>
                 <el-form-item label="新密码" prop="newPassword">
                   <el-input show-password v-model="changePasswordForm.newPassword" ></el-input>
                 </el-form-item>
                 <el-form-item label="确认密码" prop="confirmPassword">
                   <el-input show-password v-model="changePasswordForm.confirmPassword"></el-input>
                 </el-form-item>
                 <el-form-item>
                   <el-button @click="resetForm('changePasswordForm')">重置</el-button>
                 </el-form-item>
               </el-form>
               <div slot="footer" class="dialog-footer">
                 <el-button @click="dialogChangePasswordFormVisible = false">取 消</el-button>
                 <el-button type="primary" @click="submitForm('changePasswordForm')">确定</el-button>
               </div>
             </el-dialog>
             <!--修改用户邮箱-->
             <el-button type="warning" plain @click="openChangeEmailDialog" icon="el-icon-edit-outline" style="margin: 5px">修改邮箱</el-button>
             <el-dialog title="修改邮箱" :visible.sync="dialogChangeEmailFormVisible" width="500px">
               <el-steps :active="step" finish-status="success" simple style="margin-top: 20px">
                 <el-step title="步骤 1" ></el-step>
                 <el-step title="步骤 2" ></el-step>
                 <el-step title="步骤 3" ></el-step>
               </el-steps>
               <div style="height: 30px"></div>

               <div v-show="step===3">
                 <h3>邮箱修改完成！^_^</h3>
               </div>
               <el-form v-show="step!==3" :model="changeEmailForm" :rules="changeEmailFormStep2Rules" :ref="step===1?'changeEmailFormStep1':'changeEmailFormStep2'" label-width="100px" class="demo-ruleForm">
                 <el-form-item v-if="step===1" label="旧邮箱">
                   <el-input disabled v-model="userInfo.email" ></el-input>
                 </el-form-item>
                 <el-form-item v-else-if="step===2" label="新邮箱" prop="email">
                   <el-input v-model="changeEmailForm.email" ></el-input>
                 </el-form-item>

                 <el-form-item label="验证码" prop="validationCode">
                   <el-input v-model="changeEmailForm.validationCode" style="width: 250px;float: left"></el-input>
                   <el-button type="primary" size="small" @click="sendValidationCode"
                              :disabled="sendEmailLoading"
                              v-loading="sendEmailLoading"
                              :element-loading-text="loadingTime+'s后重试'"
                              element-loading-spinner="el-icon-loading"
                              element-loading-background="rgba(0, 0, 0, 0.8)"
                   >发送验证码</el-button>
                 </el-form-item>
               </el-form>
               <div slot="footer" class="dialog-footer">
                 <el-button :type="step!==3?'':'primary'" @click="dialogChangeEmailFormVisible = false" v-text="step!==3?'取 消':'完 成'"></el-button>
                 <el-button v-if="step === 1" type="primary" @click="submitForm('changeEmailFormStep1')">下一步</el-button>
                 <el-button v-if="step === 2" type="primary" @click="submitForm('changeEmailFormStep2')">确 定</el-button>
               </div>
             </el-dialog>
           </el-row>

            <!--TODO 注销账户-->
            <el-button type="danger" plain @click="openCancelAccountDialog" icon="el-icon-circle-close" style="margin: 5px">注销账户</el-button>
            <el-dialog title="注销账号" :visible.sync="dialogCancelAccountFormVisible" width="500px">
              <el-steps :active="step" finish-status="success" simple style="margin-top: 20px">
                <el-step title="步骤 1" ></el-step>
                <el-step title="步骤 2" ></el-step>
                <el-step title="步骤 3" ></el-step>
              </el-steps>
              <div style="height: 30px"></div>


              <el-form v-show="step===1" :model="cancelAccountForm" ref="cancelAccountForm" label-width="100px" class="demo-ruleForm">
                <el-form-item v-if="step===1" label="您的邮箱">
                  <el-input disabled v-model="userInfo.email" ></el-input>
                </el-form-item>

                <el-form-item label="验证码" prop="validationCode">
                  <el-input v-model="cancelAccountForm.validationCode" style="width: 250px;float: left"></el-input>
                  <el-button type="primary" size="small" @click="sendValidationCode"
                             :disabled="sendEmailLoading"
                             v-loading="sendEmailLoading"
                             :element-loading-text="loadingTime+'s后重试'"
                             element-loading-spinner="el-icon-loading"
                             element-loading-background="rgba(0, 0, 0, 0.8)"
                  >发送验证码</el-button>
                </el-form-item>
              </el-form>
              <div v-show="step===2">
                <h3>注销账号后不能反悔了哦</h3>
                <ul>
                  <li style="list-style-type: none">用户上传的文件将被清除</li>
                  <li style="list-style-type: none">用户的个人信息将被清除</li>
                </ul>
              </div><div v-show="step===3">
              <h2>注销账号成功</h2>
              <h4>即将前往登录页面</h4>
            </div>
              <div slot="footer" class="dialog-footer">
                <el-button v-if="step !== 3" type="primary" @click="dialogCancelAccountFormVisible = false" >取 消</el-button>
                <el-button v-if="step === 1" type="primary" @click="submitForm('cancelAccountForm')">下一步</el-button>
                <el-button v-if="step === 2" type="primary" @click="cancelAccount">确 定</el-button>
              </div>
            </el-dialog>
          </el-col>
        </el-row>
      </el-main>

      <el-footer>
        <el-row v-show="op===1||op===2">
          <el-col :span="2" :offset="4">
            <el-button type="primary" @click="createDir" size="small" icon="el-icon-folder-add" v-show="op===1">新建目录</el-button>
          </el-col>
          <el-col :span="6" :offset="op===1?6:12">
            <el-pagination
                background
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
    var validatePassword = (rule, value, callback) =>{
      const regex = /^[a-zA-Z]\w{5,17}$/
      if (!value) {
        return callback(new Error('密码不能为空'));
      }
      setTimeout(() => {
        if (!regex.test(value)) {
          callback(new Error("以字母开头，长度在6~18之间，只能包含字母、数字和下划线"));
        } else {
          callback();
        }
      }, 500)
    }
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
      loading: false,
      uploadFileList: [],
      uploadAvatarList: [],
      curDir: '',
      dirList: [],
      fileKeyWord: '',
      searchTableData: [],
      dialogTableVisible: false,
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
      this.$http({
        url: '/file/' + currentPage + '/' + pageSize,
        headers: {
          'token': localStorage.getItem("token")
        },
        method: 'GET',
        params: {
          curDir: this.curDir,
          op: this.op
        }
      }).then(res => {
        this.tableData = res.data.data.filePage.records
        this.pageInfo.total = res.data.data.filePage.total
        this.pageInfo.size = res.data.data.filePage.size
        this.pageInfo.current = res.data.data.filePage.current
        this.pageInfo.pages = res.data.data.filePage.pages
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
    //个人中心
    shiftUserCenter(){
      this.multipleSelection = []
      document.querySelector("#userCenter").click()
      this.op=3
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