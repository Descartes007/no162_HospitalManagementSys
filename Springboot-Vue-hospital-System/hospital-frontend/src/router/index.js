import Vue from "vue";
import VueRouter from "vue-router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import "@/assets/css/global.css";
import Login from "@/views/Login.vue";
import AdminHome from "@/views/admin/AdminHome.vue";
import AdminUser from "@/views/admin/AdminUser.vue";
import DoctorUser from "@/views/doctor/DoctorUser.vue";
import Patient from "@/views/patient/PatientUser.vue";
import PatientList from "@/views/patient/PatientList.vue";
import DoctorList from "@/views/doctor/DoctorList.vue";
import OrderList from "@/views/order/OrderList.vue";
import {getToken} from "@/utils/storage.js";
import OrderOperate from "@/views/order/OrderOperate.vue";
import SectionMessage from "@/views/section/SectionMessage.vue";
import MyOrder from "@/views/patient/MyOrder.vue";
import OrderToday from "@/views/order/OrderToday.vue";
import DealOrder from "@/views/doctor/DealOrder.vue";
import DrugList from "@/views/drug/DrugList.vue";
import CheckList from "@/views/check/CheckList.vue";
import DoctorOrder from "@/views/doctor/DoctorOrder.vue";
import InBed from "@/views/bed/InBed.vue";
import ArrangeIndex from "@/views/arrange/ArrangeIndex.vue";
import SectionList from "@/views/section/SectionList.vue";

import DoctorHome from "@/views/doctor/DoctorHome.vue";
import PatientHome from "@/views/patient/PatientHome.vue";
import MyBed from "@/views/patient/MyBed.vue";
import BedList from "@/views/bed/BedList.vue";
import DataExpore from "@/views/DataExpore.vue";
import echarts from 'echarts';//引入echarts
import DealOrderAgain from "@/views/doctor/DealOrderAgain.vue";
import DoctorCard from "@/views/doctor/DoctorCard.vue";
import PatientCard from "@/views/patient/PatientCard.vue"

Vue.prototype.$echarts = echarts;//引入echarts
Vue.use(ElementUI);
Vue.use(VueRouter);

const routes = [
  {
    path: "*",
    redirect:"/login"

  },
  {
    path: "/login",
    //redirect: "/login",//设置默认跳转路径
    component: Login
  },
  {
    path: "/admin",
    component: AdminUser,
    meta: {
      requireAuth: true,
    },
    children:[
      {
        path: "/adminLayout",
        component: AdminHome,
        meta: {
          requireAuth: true,
        },
      },
      {
      path: "/doctorList",
      component: DoctorList,
      meta: {
        requireAuth: true,
      },
    },
    {
      path: "/patientList",
      component: PatientList,
      meta: {
        requireAuth: true,
      },
    },
    {
      path: "/orderList",
      component: OrderList,
      meta: {
        requireAuth: true,
      },
    },
    {
      path: "/drugList",
      component: DrugList,
      meta: {
        requireAuth: true,
      },
    },
    {
      path: "/checkList",
      component: CheckList,
      meta: {
        requireAuth: true,
      },

    },
    {
      path: "/bedList",
      component: BedList,
      meta: {
        requireAuth: true,
      },

    },
    {
      path: "/dataExpore",
      component: DataExpore,
      meta: {
        requireAuth: true,
      },

    },
    {
      path: "/arrangeIndex",
      component: ArrangeIndex,
      meta: {
        requireAuth: true,
      },
    },
    {
      path: "/sectionList",
      component: SectionList,
      meta: {
        requireAuth: true,
      },
    },
  ]
  },
  {
    path: "/patient",
    component: Patient,
    meta: {
      requireAuth: true,
    },
    children:[
      {
        path: "/patientLayout",
        component: PatientHome,
        meta: {
          requireAuth: true,
        },
      },
      {
        path: "/orderOperate",
        component: OrderOperate
      },
      {
        path: "/sectionMessage",
        component: SectionMessage
      },
      {
        path: "/myOrder",
        component: MyOrder
      },
      {
        path: "/myBed",
        component: MyBed
      },
      {
        path: "/patientCard",
        component: PatientCard,
      }
    ]
  },
  {
    path: "/doctor",
    component: DoctorUser,
    meta: {
      requireAuth: true,
    },
    children:[
      {
        path: "/doctorLayout",
        component: DoctorHome,
        meta: {
          requireAuth: true,
        },
      },
      {
        path: "/orderToday",
        component: OrderToday,
    },
    {
      path: "/dealOrder",
      component: DealOrder
    },
    {
      path: "/dealOrderAgain",
      component: DealOrderAgain
    },
    {
      path: "/doctorOrder",
      component: DoctorOrder,
  },
  {
    path: "/inBed",
    component: InBed,
},
{
  path: "/doctorCard",
  component: DoctorCard,
}
    ],

  }
];

const router = new VueRouter({
  routes
});
//没登录的情况下，访问任何一个页面都会返回登录页面
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    const token = getToken();
    if (token !== null) {
      //直接放行
      next();
  } else {
      next("/login");
    }
  }
  else{
    next();
  }
  });
export default router;
