<template>
  <div class="container mt-5">
    <div class="row">
      <!-- 左侧照片区域 -->
      <div class="col-md-4">
        <div class="card shadow-lg">
          <div class="card-body text-center">
            <h5 class="card-title">UID:{{ $store.state.user.username }}</h5>
            <img :src="$store.state.user.photo" alt="" style="width: 80%;height:80%">
            <p class="card-text">个人简介</p>
          </div>
        </div>
      </div>

      <!-- 右侧个人信息区域 -->
      <div class="col-md-8">
        <div class="card shadow-lg">
          <div class="card-header bg-primary text-white text-center">
            <h4>我的Bot</h4>
          </div>
          <div class="card-body">
            <!-- 创建Bot按钮 -->
            <div class="d-flex justify-content-end mb-3">
              <button class="btn btn-success" @click="openModal">
                <i class="fas fa-plus-circle"></i> 创建Bot
              </button>
            </div>

            <!-- Bot列表 -->
            <div v-for="(bot, index) in bots" :key="index" class="mb-3 p-3 border rounded shadow-sm bg-light">
              <div class="d-flex justify-content-between align-items-center">
                <div>
                  <h5 class="mb-1 text-truncate" style="max-width: 200px; font-size: 1.1rem; font-weight: 600;">BOT名称:{{ bot.title }}</h5>
                  <small class="text-muted">创建于: {{ bot.createtime }}</small>
                </div>
                <div>
                  <button class="btn btn-outline-warning btn-sm me-2" @click="editBot(index)">
                    <i class="fas fa-edit"></i> 编辑
                  </button>
                  <button class="btn btn-outline-danger btn-sm" @click="deleteBot(index)">
                    <i class="fas fa-trash-alt"></i> 删除
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Bot创建/编辑Modal -->
    <div class="modal fade" id="botModal" tabindex="-1" aria-labelledby="botModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title" id="botModalLabel">{{ isEdit ? '编辑Bot' : '创建Bot' }}</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="submitBot">
              <div class="mb-3">
                <label for="botName" class="form-label">名称</label>
                <input type="text" id="botName" v-model="newBot.title" class="form-control" required>
              </div>
              <div class="mb-3">
                <label for="botDescription" class="form-label">简介</label>
                <textarea id="botDescription" v-model="newBot.description" class="form-control" rows="3"></textarea>
              </div>
              <div class="mb-3">
                <label for="botCode" class="form-label">代码</label>
                <div ref="codeEditor" style="height: 200px;">
                  <VAceEditor
                    v-model="newBot.content" 
                    @init="initCodeEditor"
                    lang="c_cpp"
                    theme="textmate"
                    style="height: 300px" />
                </div>  <!-- 代码编辑器的容器 -->
              </div>
              <button type="submit" class="btn btn-primary w-100">{{ isEdit ? '保存修改' : '创建Bot' }}</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { ref, reactive, onMounted, nextTick} from 'vue';
import { Modal } from 'bootstrap/dist/js/bootstrap';  // 导入Modal
import ace from 'ace-builds';  // 导入Ace代码编辑器
import $ from 'jquery';
import { useStore } from 'vuex';


export default {
  setup() {
    const showModal = ref(false);
    const isEdit = ref(false);
    const store = useStore();
    const newBot = reactive({
      title: '',
      description: '',
      content: "",
      id:"",
    });
    let editing_id = 0;
    let bots = reactive([]);

    // 刷新Bot列表
    const refresh_bots = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
           let tmp = [];
           for (let k of resp)
           {
              tmp.push(k);
           }
           Object.assign(bots, tmp);
        }
      });
    };

    refresh_bots();

    const codeEditor = ref(null);
    let editorInstance = null;

    // 初始化代码编辑器
    const initCodeEditor = () => {
      nextTick(() => {
        if (codeEditor.value) {
          editorInstance = ace.edit(codeEditor.value);
          editorInstance.setTheme("ace/theme/monokai");
          editorInstance.session.setMode("ace/mode/javascript");
        } else {
          console.error('Cannot find code editor container!');
        }
      });
    };

    // 打开Modal并清空数据，设置为创建模式
    const openModal = () => {
      newBot.title = '';
      newBot.description = '';
      newBot.content = '';
      editorInstance = ace.edit(codeEditor.value);
      editorInstance.setValue("");
      isEdit.value = false;
      showModal.value = true;
      nextTick(() => {
        const modalElement = document.getElementById('botModal');
        const modal = new Modal(modalElement);
        modal.show();
      });
    };

    const clearBot = ()=>{
        newBot.content = "";
        newBot.description = "";
        newBot.title = "";
    }
    
    // 编辑现有Bot
    const editBot = (index) => {
      clearBot();
      editing_id = index;
      Object.assign(newBot, bots[index]);
      editorInstance = ace.edit(codeEditor.value);
      editorInstance.setValue(newBot.content || "");
      isEdit.value = true;
      showModal.value = true;
      nextTick(() => {
        const modalElement = document.getElementById('botModal');
        const modal = new Modal(modalElement);
        modal.show();
      });
    };


    // 提交创建或编辑Bot
    const submitBot = () => {
      // 从编辑器获取最新的代码内容
      if (editorInstance) {
        newBot.content = editorInstance.getValue(); // 获取代码编辑器的内容
      }

      if (isEdit.value) {
        const index = editing_id;
        Object.assign(bots[index], newBot);
        $.ajax({
          url:"http://127.0.0.1:3000/user/bot/update/",
          type: "POST",
          data:{
               id: newBot.id,
               title:newBot.title,
               description:newBot.description,
               content:newBot.content
          },
          headers: {
          Authorization: "Bearer " + store.state.user.token,
          },
          success(resp){
                console.log(resp);
                resp;
                Object.assign(bots[index], newBot);
                refresh_bots();
          },
          error()
          {

          },
        })
      } else {
          const newBotEntry = {
          ...newBot,
        };
        $.ajax({
          url:"http://127.0.0.1:3000/user/bot/add/",
          type:"POST",
          data:{
            title:newBotEntry.title,
            description:newBotEntry.description,
            content:newBotEntry.content,
          },
          headers: {
          Authorization: "Bearer " + store.state.user.token,
          },
          success(resp){
            console.log(resp);
            bots.push(newBotEntry);
            refresh_bots();
          },
          error(){

          }
        })
      }
      clearBot();
      closeModal();
    };

    // 关闭Modal
    const closeModal = () => {
      const modalElement = document.getElementById('botModal');
      const modalInstance = Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide();
      } else {
        console.error('Modal instance not found!');
      }
    };

    // 删除Bot
    const deleteBot = (index) => {
      if (confirm('确定要删除这个Bot吗？')) {
        $.ajax({
          url:"http://127.0.0.1:3000/user/bot/remove",
          type:"POST",
          data:{
            id:bots[index].id,
          },
          headers: {
          Authorization: "Bearer " + store.state.user.token,
          },
          success(resp){
             console.log(resp);
          },
          error(){

          }
        })
        bots.splice(index, 1);
      }
    };

    // 在组件挂载时初始化代码编辑器
    onMounted(() => {
      initCodeEditor();
    });

    return {
      showModal,
      isEdit,
      newBot,
      bots,
      openModal,
      editBot,
      submitBot,
      closeModal,
      deleteBot,
      codeEditor,
      editing_id,
    };
  }
};
</script>



<style scoped>
.container {
  max-width: 1200px;
}

.card {
  border-radius: 12px;
}

.card-header {
  font-size: 1.5rem;
  font-weight: bold;
}

.modal-content {
  border-radius: 12px;
}

button {
  font-size: 1rem;
}

/* 美化布局 */
.bg-primary {
  background-color: #3498db !important;
}

.bg-light {
  background-color: #ecf0f1 !important;
}

.modal-dialog {
  max-width: 900px;
}

button {
  font-size: 1rem;
}

.card-body h5 {
  font-size: 1.25rem;
  font-weight: bold;
}

.card-body p {
  font-size: 0.9rem;
  color: #555;
}

.text-muted {
  font-size: 0.85rem;
}

.text-truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 调整Bot名称样式 */
.card-body h5 {
  font-size: 1.15rem;
  font-weight: 600;
}

/* 居中"我的Bot"标题 */
.card-header {
  text-align: center;
}

/* 右对齐创建Bot按钮 */
.d-flex.justify-content-end {
  margin-right: 20px;
}
</style>
