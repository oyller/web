<template>
  <ContentField>
    <div class="table-container">
      <table class="game-table">
        <thead>
          <tr>
            <th>A</th>
            <th>B</th>
            <th>对局结果</th>
            <th>对局时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(record, index) in records" :key="index">
            <td>
              <img :src="record.a_photo" alt="Player A" class="avatar" />
              {{ record.a_username }}
            </td>
            <td>
              <img :src="record.b_photo" alt="Player B" class="avatar" />
              {{ record.b_username }}
            </td>
            <td>{{ record.result }}</td>
            <td>{{ record.record.createtime }}</td>
            <td>
              <button @click="viewImage(record.record.id)">查看录像</button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div class="pagination">
        <button @click="changePage('prev')">前一页</button>
        <div> 当前页号:{{ currentPage }}</div>
        <button @click="changePage('next')">后一页</button>
        <div class="page-options">
          <button v-for="page in pageOptions" :key="page" @click="selectPage(page)">
            {{ page }}
          </button>
        </div>
        
        <!-- 新增前往几页输入框 -->
        <div class="go-to-page">
          <input type="number" v-model="targetPage" :max="total_pages" :min="1" placeholder="输入页码" />
          <button @click="goToPage">前往</button>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import store from '../../store/index'
import { onMounted, reactive, ref, computed } from 'vue';
import $ from 'jquery'
import router from '../../router/index'

export default {
  components: {
    ContentField,
  },
  setup(){
    let total_pages = reactive(1);
    let records = reactive([]);
    let currentPage = ref(1);
    let targetPage = ref(1); // 新增的目标页数

    // 计算分页按钮
    const pageOptions = computed(() => {
      const options = [];
      for (let i = -2; i <= 2; i++) {
        const page = i + currentPage.value;
        if (page > 1 && page <= total_pages) {
          options.push(page);
        }
      }
      return options;
    });

    const fetchRecords = (currentPage) => {
      $.ajax({
        url: "http://127.0.0.1:3000/record/record_page_get/",
        type: "GET",
        data: { 
          page:currentPage
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success: (resp) => {
          records.splice(0, records.length); // 清空数组
          for (let k of resp.records)
            records.push(k);       
          total_pages = Math.floor(resp.records_count / 10);
          if (resp.records_count % 10 !== 0) total_pages++;
        },
        error: (resp) => {
          console.error(resp);
        },
      });
    }

    onMounted(() => {
      fetchRecords(currentPage.value);
    });

    const changePage = (direction) => {
      if (direction === 'prev' && currentPage.value > 1) {
        currentPage.value--;
      } else if (direction === 'next' && currentPage.value < total_pages) {
        currentPage.value++;
      }
      fetchRecords(currentPage.value);
    }

    const selectPage = (page) => {
      currentPage.value = page;
      fetchRecords(page);
    }

    const goToPage = () => {
      // 检查输入是否有效
      if (targetPage.value >= 1 && targetPage.value <= total_pages) {
        currentPage.value = targetPage.value;
        fetchRecords(targetPage.value);
      } else {
        // 如果无效，跳转到第1页
        currentPage.value = 1;
        fetchRecords(1);
      }
      targetPage.value = 1; // 重置输入框
    }

    const stringTo2D = map => {
            let g = [];
            for (let i = 0, k = 0; i < 13; i ++ ) {
                let line = [];
                for (let j = 0; j < 14; j ++, k ++ ) {
                    if (map[k] === '0') line.push(0);
                    else line.push(1);
                }
                g.push(line);
            }
            return g;
        }


    const open_record_content = recordId =>{
      for (const record of records)
      {
         if (record.record.id === recordId)
         {
             console.log(record);
             store.commit("updateIsRecord", true);
             store.commit("update_Record_Game",{
             map:stringTo2D(record.record.map),
             a_id: record.record.aid,
             a_sx: record.record.asx,
             a_sy: record.record.asy,
             b_id: record.record.bid,
             b_sx: record.record.bsx,
             b_sy: record.record.bsy,
             });
             store.commit("updateSteps", {
               a_steps:record.record.asteps,
               b_steps:record.record.bsteps,
             });
             store.commit("updateRecordLoser", record.record.loser);

             
             router.push({
                        name: "record_viedo_index",
                        params: {
                            recordId
                        }
                        })
                    break;
            
          }
        
      }

    }


    const viewImage = (recordId) => {
      open_record_content(recordId);
    }

    return {
      total_pages,
      records,
      currentPage,
      pageOptions,
      targetPage, // 新增的响应式数据
      changePage,
      selectPage,
      goToPage, // 新增的跳转函数
      viewImage,
    }
  }
}
</script>

<style scoped>
.table-container {
  margin: 20px;
  overflow-x: auto;
  box-shadow: 0 1px 3px rgba(0,0,0,0.12);
  border-radius: 10px;
  background: white;
}

.game-table {
  width: 100%;
  border-collapse: collapse;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.game-table th {
  background: linear-gradient(145deg, #6b48ff, #4a36d1);
  color: white;
  padding: 15px;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 0.9em;
}

.game-table td {
  padding: 12px;
  color: #444;
  font-size: 0.95em;
}

.game-table tr:nth-child(even) {
  background-color: #f8f9fa;
}

.game-table tr:hover {
  background-color: #f1f3f5;
  transition: background 0.3s ease;
}

.avatar {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  vertical-align: middle;
  margin-right: 10px;
  border: 2px solid #e0e0e0;
}

button {
  padding: 8px 16px;
  border: none;
  border-radius: 25px;
  background: #6b48ff;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9em;
  margin: 2px;
}

button:hover {
  background: #4a36d1;
  transform: translateY(-1px);
  box-shadow: 0 3px 8px rgba(106,72,255,0.3);
}

.pagination {
  margin: 25px 0;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
}

.page-options button {
  background: #f1f3f5;
  color: #444;
  min-width: 36px;
}

.page-options button:hover {
  background: #e9ecef;
}

.pagination > button {
  padding: 8px 20px;
}

.pagination > span {
  font-weight: 600;
  color: #6b48ff;
  min-width: 30px;
  text-align: center;
}

/* 新增样式：前往几页输入框 */
.go-to-page {
  display: flex;
  gap: 8px;
  align-items: center;
}

.go-to-page input {
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1em;
  width: 80px;
}

.go-to-page button {
  padding: 6px 12px;
  background: #6b48ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.go-to-page button:hover {
  background: #4a36d1;
}
</style>
