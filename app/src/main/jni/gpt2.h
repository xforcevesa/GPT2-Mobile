#ifndef GPT2_H
#define GPT2_H

#include <map>
#include <net.h>
#include <vector>

class GPT2
{
public:
    GPT2();

    int load(AAssetManager* mgr, std::string vocab);
    std::string chat(std::string in);

private:
    std::vector<int> token2idx(std::string token);
    std::string idx2token(std::vector<int> idx);

private:
    ncnn::Net net;
    ncnn::UnlockedPoolAllocator blob_pool_allocator;
    ncnn::PoolAllocator workspace_pool_allocator;

    std::map<std::wstring, int> tokenizer_token2idx;
    std::map<int, std::wstring> tokenizer_idx2token;

    const int max_history_len = 3;
    const int max_len = 25;

    std::vector<std::vector<int>> history;
};

#endif // NANODET_H
