<template>
    <div class="aboutMe">
        <mdView :content="content"></mdView>
    </div>
</template>
<script>
import {getMe} from '../../api/about'
export default {
    data(){
        return{
            content:"",
        }
    },
    created(){
        getMe().then(res=>{
            this.content=res.data.personResume
        })        
    },
    components:{
        mdView:resolve=>require(['../editor/mdView'],resolve)
    }
}
</script>